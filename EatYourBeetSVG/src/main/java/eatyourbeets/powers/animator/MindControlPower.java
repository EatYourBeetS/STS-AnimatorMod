package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.interfaces.subscribers.OnDamageActionSubscriber;
import eatyourbeets.orbs.animator.*;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MindControlPower extends AnimatorClickablePower implements OnDamageActionSubscriber, OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(MindControlPower.class);
    public static final Color ACTIVE_COLOR = new Color(0.5f, 1f, 0.5f, 1f);
    public static final int AFFINITY_GAIN = 2;
    public static final int BUFF_DEBUFF_GAIN = 1;
    public static final HashMap<String, OrbConstructor> ORB_MAP = new HashMap<>();

    protected final AbstractCard sourceCard;

    public boolean active;
    public boolean canIntercept;
    public boolean canRedirect;
    public int block;
    public int damage;
    public ArrayList<Affinity> affinityPowers = new ArrayList<>();
    public ArrayList<OrbConstructor> orbs = new ArrayList<>();
    public ArrayList<PowerHelper> buffs = new ArrayList<>();
    public ArrayList<PowerHelper> debuffs = new ArrayList<>();
    protected byte moveByte;
    protected AbstractMonster.Intent moveIntent;
    protected EnemyMoveInfo move;
    protected int lastDamage;
    protected int lastMultiplier;
    protected boolean lastIsMultiDamage;

    static {
        ORB_MAP.put(GR.Tooltips.Air.id, new OrbConstructor(GR.Tooltips.Air, Air::new, 3));
        ORB_MAP.put(GR.Tooltips.Chaos.id, new OrbConstructor(GR.Tooltips.Chaos, Chaos::new, 5));
        ORB_MAP.put(GR.Tooltips.Dark.id, new OrbConstructor(GR.Tooltips.Dark, Dark::new, 1));
        ORB_MAP.put(GR.Tooltips.Earth.id, new OrbConstructor(GR.Tooltips.Earth, Earth::new, 3));
        ORB_MAP.put(GR.Tooltips.Fire.id, new OrbConstructor(GR.Tooltips.Fire, Fire::new, 2));
        ORB_MAP.put(GR.Tooltips.Frost.id, new OrbConstructor(GR.Tooltips.Frost, Frost::new, 2));
        ORB_MAP.put(GR.Tooltips.Lightning.id, new OrbConstructor(GR.Tooltips.Lightning, Lightning::new, 1));
        ORB_MAP.put(GR.Tooltips.Plasma.id, new OrbConstructor(GR.Tooltips.Plasma, Plasma::new, 6));
        ORB_MAP.put(GR.Tooltips.Water.id, new OrbConstructor(GR.Tooltips.Water, Water::new, 5));
    }

    private static ArrayList<Affinity> GetAffinityPowers(AbstractCard card, int limit) {
        ArrayList<Affinity> affs = new ArrayList<>();
        if (card instanceof EYBCard) {
            for (EYBCardTooltip tip : ((EYBCard) card).tooltips)
            {
                Affinity foundAf = JUtils.Find(Affinity.Extended(), af -> af.GetTooltip().id.equals(tip.id));
                if (foundAf != null) {
                    affs.add(foundAf);
                    if (affs.size() >= limit) {
                        break;
                    }
                }
            }
        }

        return affs;
    }

    private static ArrayList<PowerHelper> GetCommonBuffs(AbstractCard card, int limit) {
        return GetCommonPowers(card, limit,  GameUtilities.GetCommonBuffs());
    }

    private static ArrayList<PowerHelper> GetCommonDebuffs(AbstractCard card, int limit) {
        return GetCommonPowers(card, limit,  GameUtilities.GetCommonDebuffs());
    }

    private static ArrayList<PowerHelper> GetCommonPowers(AbstractCard card, int limit, ArrayList<PowerHelper> source) {
        ArrayList<PowerHelper> powers = new ArrayList<>();
        if (card instanceof EYBCard) {
            for (EYBCardTooltip tip : ((EYBCard) card).tooltips)
            {
                PowerHelper foundPower = JUtils.Find(source, ph -> ph.Tooltip.id.equals(tip.id));
                if (foundPower != null) {
                    powers.add(foundPower);
                    if (powers.size() >= limit) {
                        break;
                    }
                }
            }
        }

        return powers;
    }

    private static ArrayList<OrbConstructor> GetOrbs(AbstractCard card, int limit) {
        ArrayList<OrbConstructor> orbs = new ArrayList<>();
        if (card instanceof EYBCard) {
            for (EYBCardTooltip tip : ((EYBCard) card).tooltips)
            {
                OrbConstructor found = ORB_MAP.get(tip.id);
                if (found != null) {
                    orbs.add(found);
                    if (orbs.size() >= limit) {
                        break;
                    }
                }
            }
        }
        return orbs;
    }

    public MindControlPower(AbstractCreature owner, AbstractCard sourceCard)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.Affinity, 0);
        this.sourceCard = sourceCard;
        this.triggerCondition.SetCheckCondition(__ -> JUtils.Find(owner.powers, po -> po instanceof MindControlPower && ((MindControlPower) po).active) == null);
        DetermineEffect(sourceCard);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
        CombatStats.onDamageAction.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        CombatStats.onDamageAction.Unsubscribe(this);

        GameActions.Last.Callback(() ->
        {
           if (!owner.powers.contains(this))
           {
               owner.powers.add(this);
               Collections.sort(owner.powers);
           }
        });

        GameActions.Bottom.MakeCardInHand(sourceCard);
    }

    @Override
    public void onDeath()
    {
        GameActions.Bottom.MakeCardInHand(sourceCard);
        super.onDeath();
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();
        active = false;
        owner.flipHorizontal = false;
        AbstractMonster m = JUtils.SafeCast(this.owner, AbstractMonster.class);
        if (m != null && this.moveIntent != null) {
            m.setMove(this.moveByte, this.moveIntent, this.lastDamage, this.lastMultiplier, this.lastIsMultiDamage);
            m.createIntent();
            m.applyPowers();
            moveIntent = null;
        }
    }

    @Override
    public String GetUpdatedDescription()
    {
        Affinity first = triggerCondition.affinities.length > 0 ? triggerCondition.affinities[0] : Affinity.General;



        StringBuilder sb = new StringBuilder();
        if (damage > 0) {
            sb.append(JUtils.Format(powerStrings.DESCRIPTIONS[1], damage));
        }
        if (block > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(JUtils.Format(powerStrings.DESCRIPTIONS[3], block, GR.Tooltips.Block));
        }
        if (affinityPowers.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(JUtils.Format(powerStrings.DESCRIPTIONS[3], AFFINITY_GAIN, StringUtils.join(JUtils.Map(affinityPowers, aff -> aff.GetTooltip().id))));
        }
        if (buffs.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(JUtils.Format(powerStrings.DESCRIPTIONS[3], BUFF_DEBUFF_GAIN, StringUtils.join(JUtils.Map(buffs, buff -> buff.Tooltip.id))));
        }
        if (debuffs.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(JUtils.Format(powerStrings.DESCRIPTIONS[2], BUFF_DEBUFF_GAIN, StringUtils.join(JUtils.Map(debuffs, debuff -> debuff.Tooltip.id))));
        }
        if (orbs.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(JUtils.Format(powerStrings.DESCRIPTIONS[3], BUFF_DEBUFF_GAIN, StringUtils.join(JUtils.Map(orbs, orb -> orb.Tooltip.id))));
        }
        if (canRedirect) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(powerStrings.DESCRIPTIONS[4]);
        }
        if (canIntercept) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(powerStrings.DESCRIPTIONS[5]);
        }

        return FormatDescription(0
                , sourceCard.name.replace(" ", " #y")
                , triggerCondition.requiredAmount
                , first.GetTooltip(), sb.toString()
                , active ? powerStrings.DESCRIPTIONS[6] : !triggerCondition.checkCondition.Invoke(triggerCondition.requiredAmount) ? powerStrings.DESCRIPTIONS[7] : "");
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {

        Color borderColor = active ? ACTIVE_COLOR : (enabled && triggerCondition.CanUse()) ? c : disabledColor;
        Color imageColor = enabled ? c : disabledColor;

        super.renderIconsImpl(sb, x, y, borderColor, imageColor);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        active = true;
        owner.flipHorizontal = true;

        boolean isAttacking = damage > 0;
        boolean isDefending = block > 0;
        boolean isDebuffing = !debuffs.isEmpty();
        boolean isBuffing = !buffs.isEmpty() || !orbs.isEmpty();

        final AbstractMonster monster = JUtils.SafeCast(owner, AbstractMonster.class);
        if (monster != null && (isAttacking || isDefending || isDebuffing || isBuffing))
        {
            this.moveByte = monster.nextMove;
            this.moveIntent = monster.intent;
            GameActions.Last.Callback(() -> {
                try {
                    Field f = AbstractMonster.class.getDeclaredField("move");
                    f.setAccessible(true);
                    this.move = (EnemyMoveInfo)f.get(monster);
                    this.lastDamage = this.move.baseDamage;
                    this.lastMultiplier = this.move.multiplier;
                    this.lastIsMultiDamage = this.move.isMultiDamage;
                    this.move.baseDamage = damage;
                    this.move.multiplier = 0;
                    this.move.isMultiDamage = false;

                    if (isAttacking) {
                        if (isBuffing) {
                            this.move.intent = AbstractMonster.Intent.ATTACK_BUFF;
                        }
                        else if (isDebuffing) {
                            this.move.intent = AbstractMonster.Intent.ATTACK_DEBUFF;
                        }
                        else {
                            this.move.intent = AbstractMonster.Intent.ATTACK;
                        }
                    }
                    else if (isDefending) {
                        if (isBuffing) {
                            this.move.intent = AbstractMonster.Intent.DEFEND_BUFF;
                        }
                        else if (isDebuffing) {
                            this.move.intent = AbstractMonster.Intent.DEFEND_DEBUFF;
                        }
                        else {
                            this.move.intent = AbstractMonster.Intent.DEFEND;
                        }
                    }
                    else if (isBuffing) {
                        if (isDebuffing) {
                            this.move.intent = AbstractMonster.Intent.STRONG_DEBUFF;
                        }
                        else if (!buffs.isEmpty()) {
                            this.move.intent = AbstractMonster.Intent.BUFF;
                        }
                        else {
                            this.move.intent = AbstractMonster.Intent.MAGIC;
                        }
                    }
                    else {
                        this.move.intent = AbstractMonster.Intent.DEBUFF;
                    }

                    monster.createIntent();
                } catch (NoSuchFieldException | IllegalAccessException var2) {
                    JUtils.LogWarning(this, "Failed to create intent");
                }
            });
        }
    }

    public boolean DoActions() {
        if (damage > 0) {
            GameActions.Bottom.DealDamage(owner, GameUtilities.GetRandomEnemy(true), damage, DamageInfo.DamageType.NORMAL, AttackEffects.BLUNT_HEAVY);
        }
        if (block > 0) {
            GameActions.Bottom.StackPower(new NextTurnBlockPower(player, block));
        }
        for (Affinity af : affinityPowers) {
            GameActions.Bottom.StackAffinityPower(af, AFFINITY_GAIN, false);
        }
        for (PowerHelper ph : buffs) {
            GameActions.Bottom.ApplyPower(TargetHelper.Player(), ph, BUFF_DEBUFF_GAIN);
        }
        for (PowerHelper ph : debuffs) {
            GameActions.Bottom.ApplyPower(TargetHelper.RandomEnemy(), ph, BUFF_DEBUFF_GAIN);
        }
        for (OrbConstructor o : orbs) {
            GameActions.Bottom.ChannelOrbs(o.OrbInvoker, 1);
        }

        return damage <= 0 && block <= 0 && buffs.isEmpty() && debuffs.isEmpty() && orbs.isEmpty();
    }


    /* Effects and costs are determined as follows

    Let R = 3 if rare/special, 2 if uncommon/Curse, 1 otherwise. +1 if card is High-Cost

    If the card is an ATTACK:
        1. Deal M damage to a random target besides you, where M = Monster base damage * (50 + card damage) / 50
        2. If the card has Common Debuff tooltips, apply the first R to that target

        COST is 4 + (M / 15) +1 if Common Debuffs are present.
    If the card is a SKILL/POWER:
        1. If the card has Common Debuff tooltips, apply them to a random target besides you
        2. If card has Common Buff or Affinity power tooltips, it gives you that power. The total number of Common Debuff and Buffs cannot exceed R
        3. If the card has Block, you gain M Block, where M = Monster max health * (card block) / 100 and cannot exceed card block
        4. If the card has Orbs, you channel 1 Orb

        COST is 4 + (M / 15) + buff/debuff count + orbs.
        5. If none of the above actions is applicable, monster will target the enemy if attacking/debuffing, or you if buffing

        COST is 10 General
    If the card is a CURSE/OTHER:
        1. If the card has Common Debuff tooltips, apply the first R to a random target besides you
        COST is 4 + R
        2. Otherwise, ALL other enemies will target this enemy
        Cost is 8

    COLOR of the cost is the highest leftmost affinity. If not present or Star, use General


    * */
    private void DetermineEffect(AbstractCard card) {
        int rarityModifier = GameUtilities.IsHighCost(card) ? 2 : 1;
        switch (card.rarity) {
            case RARE:
            case SPECIAL:
                rarityModifier += 2;
                break;
            case UNCOMMON:
            case CURSE:
                rarityModifier += 1;
                break;
        }

        switch (card.type) {
            case ATTACK:
                damage = GetBaseDamage() * (50 + card.baseDamage) / 50;
                debuffs = GetCommonDebuffs(card, rarityModifier);

                this.triggerCondition.requiredAmount = 4 + debuffs.size() + damage / 15;
                break;
            case SKILL:
            case POWER:
                block = Math.min(owner.maxHealth * card.baseBlock / 100, card.baseBlock);
                affinityPowers = GetAffinityPowers(card, rarityModifier);
                buffs = GetCommonBuffs(card, rarityModifier -= affinityPowers.size());
                debuffs = GetCommonDebuffs(card, rarityModifier -= buffs.size());
                orbs = GetOrbs(card, rarityModifier -= debuffs.size());

                if (block > 0 || affinityPowers.size() > 0 || buffs.size() > 0 || debuffs.size() > 0 || orbs.size() > 0) {
                    this.triggerCondition.requiredAmount = 4 + debuffs.size() + buffs.size() + affinityPowers.size() + block / 15 + (int) JUtils.Sum(orbs, o -> (float) o.cost);
                }
                else {
                    canRedirect = true;
                    this.triggerCondition.requiredAmount = 7;
                }

                break;
            default:
                debuffs = GetCommonDebuffs(card, rarityModifier - buffs.size());
                if (debuffs.size() > 0) {
                    this.triggerCondition.requiredAmount = 4 + debuffs.size();
                }
                else {
                    canIntercept = true;
                    this.triggerCondition.requiredAmount = 7;
                }
        }

        EYBCardAffinities cardAffinities = GameUtilities.GetAffinities(card);
        if (cardAffinities != null) {
            Affinity target = cardAffinities.GetLevel(Affinity.General) <= 0 || cardAffinities.GetLevel(Affinity.Star) > 0 ? Affinity.General : JUtils.FindMax(cardAffinities.List, af -> af.level).type;
            this.triggerCondition.affinities = new Affinity[] {target};
        }

    }

    protected int GetBaseDamage() {
        final AbstractMonster monster = JUtils.SafeCast(owner, AbstractMonster.class);
        if (monster != null) {
            try {
                Field f = AbstractMonster.class.getDeclaredField("move");
                f.setAccessible(true);
                this.move = (EnemyMoveInfo)f.get(monster);
                this.move.intent = AbstractMonster.Intent.ATTACK;
                ArrayList<DamageInfo> damages = monster.damage;
                if (damages == null || damages.isEmpty()) {
                    return 1;
                }
                else {
                    return damages.get(0).base;
                }
            } catch (NoSuchFieldException | IllegalAccessException var2) {
                JUtils.LogWarning(this, "Monster damage could not be obtained");
            }
        }
        return 1;
    }



    @Override
    public void OnDamageAction(AbstractGameAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        if (active) {
            boolean isOwner = action.source.powers.contains(this);
            if (isOwner && canRedirect) {
                AbstractCreature newT = GameUtilities.GetRandomEnemy(true);
                if (newT == null) {
                    newT = action.source;
                }
                info.applyPowers(action.source, newT);
                action.target = newT;
            }
            else if (!isOwner && canIntercept) {
                info.applyPowers(action.source, owner);
                action.target = owner;
            }
        }
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (active) {
            boolean isOwner = action.source.powers.contains(this);
            if (isOwner && canRedirect) {
                AbstractCreature newT = GameUtilities.GetRandomEnemy(true);
                if (newT == null) {
                    newT = action.source;
                }
                power.owner = newT;
                action.target = newT;
            }
            else if (!isOwner && canIntercept) {
                power.owner = owner;
                action.target = owner;
            }
        }
        return true;
    }

    private static class OrbConstructor {
        public EYBCardTooltip Tooltip;
        public FuncT0<AbstractOrb> OrbInvoker;
        public int cost;

        public OrbConstructor(EYBCardTooltip tooltip, FuncT0<AbstractOrb> func, int cost) {
            this.Tooltip = tooltip;
            this.OrbInvoker = func;
            this.cost = cost;
        }
    }
}