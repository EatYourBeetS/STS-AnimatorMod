package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.utilities.TargetHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnDamageActionSubscriber;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class MindControlPower extends PCLClickablePower implements OnDamageActionSubscriber, OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(MindControlPower.class);
    public static final Color ACTIVE_COLOR = new Color(0.5f, 1f, 0.5f, 1f);
    public static final int AFFINITY_GAIN = 2;
    public static final int BUFF_DEBUFF_GAIN = 1;

    protected final AbstractCard sourceCard;

    public boolean active;
    public boolean canIntercept;
    public boolean canRedirect;
    public int block;
    public int damage;
    public ArrayList<PCLAffinity> affinityPowers = new ArrayList<>();
    public ArrayList<PCLOrbHelper> orbs = new ArrayList<>();
    public ArrayList<PCLPowerHelper> buffs = new ArrayList<>();
    public ArrayList<PCLPowerHelper> debuffs = new ArrayList<>();
    protected byte moveByte;
    protected AbstractMonster.Intent moveIntent;
    protected EnemyMoveInfo move;
    protected int lastDamage;
    protected int lastMultiplier;
    protected boolean lastIsMultiDamage;

    private static ArrayList<PCLAffinity> GetAffinityPowers(AbstractCard card, int limit) {
        ArrayList<PCLAffinity> affs = new ArrayList<>();
        if (card instanceof PCLCard) {
            for (PCLCardTooltip tip : ((PCLCard) card).tooltips)
            {
                PCLAffinity foundAf = PCLJUtils.Find(PCLAffinity.Extended(), af -> af.GetTooltip().id.equals(tip.id));
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

    private static ArrayList<PCLPowerHelper> GetCommonBuffs(AbstractCard card, int limit) {
        return GetCommonPowers(card, limit,  PCLGameUtilities.GetPCLCommonBuffs());
    }

    private static ArrayList<PCLPowerHelper> GetCommonDebuffs(AbstractCard card, int limit) {
        return GetCommonPowers(card, limit,  PCLGameUtilities.GetPCLCommonDebuffs());
    }

    private static ArrayList<PCLPowerHelper> GetCommonPowers(AbstractCard card, int limit, ArrayList<PCLPowerHelper> source) {
        ArrayList<PCLPowerHelper> powers = new ArrayList<>();
        if (card instanceof PCLCard) {
            for (PCLCardTooltip tip : ((PCLCard) card).tooltips)
            {
                PCLPowerHelper foundPower = PCLJUtils.Find(source, ph -> ph.Tooltip.id.equals(tip.id));
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

    private static ArrayList<PCLOrbHelper> GetOrbs(AbstractCard card, int limit) {
        ArrayList<PCLOrbHelper> orbs = new ArrayList<>();
        if (card instanceof PCLCard) {
            for (PCLCardTooltip tip : ((PCLCard) card).tooltips)
            {
                PCLOrbHelper found = PCLJUtils.Find(PCLOrbHelper.ALL.values(), o -> o.Tooltip.id.equals(tip.id));
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
        this.triggerCondition.SetCheckCondition(__ -> PCLJUtils.Find(owner.powers, po -> po instanceof MindControlPower && ((MindControlPower) po).active) == null);
        DetermineEffect(sourceCard);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
        PCLCombatStats.onDamageAction.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        PCLCombatStats.onDamageAction.Unsubscribe(this);

        PCLActions.Last.Callback(() ->
        {
           if (!owner.powers.contains(this))
           {
               owner.powers.add(this);
               Collections.sort(owner.powers);
           }
        });

        PCLActions.Bottom.MakeCardInHand(sourceCard);
    }

    @Override
    public void onDeath()
    {
        PCLActions.Bottom.MakeCardInHand(sourceCard);
        super.onDeath();
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();
        active = false;
        owner.flipHorizontal = false;
        AbstractMonster m = PCLJUtils.SafeCast(this.owner, AbstractMonster.class);
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
        PCLAffinity first = triggerCondition.affinities.length > 0 ? triggerCondition.affinities[0] : PCLAffinity.General;



        StringBuilder sb = new StringBuilder();
        if (damage > 0) {
            sb.append(PCLJUtils.Format(powerStrings.DESCRIPTIONS[1], damage));
        }
        if (block > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(PCLJUtils.Format(powerStrings.DESCRIPTIONS[3], block, GR.Tooltips.Block));
        }
        if (affinityPowers.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(PCLJUtils.Format(powerStrings.DESCRIPTIONS[3], AFFINITY_GAIN, StringUtils.join(PCLJUtils.Map(affinityPowers, aff -> aff.GetTooltip().id))));
        }
        if (buffs.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(PCLJUtils.Format(powerStrings.DESCRIPTIONS[3], BUFF_DEBUFF_GAIN, StringUtils.join(PCLJUtils.Map(buffs, buff -> buff.Tooltip.id))));
        }
        if (debuffs.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(PCLJUtils.Format(powerStrings.DESCRIPTIONS[2], BUFF_DEBUFF_GAIN, StringUtils.join(PCLJUtils.Map(debuffs, debuff -> debuff.Tooltip.id))));
        }
        if (orbs.size() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(PCLJUtils.Format(powerStrings.DESCRIPTIONS[3], BUFF_DEBUFF_GAIN, StringUtils.join(PCLJUtils.Map(orbs, orb -> orb.Tooltip.id))));
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
                , PCLJUtils.ModifyString(sourceCard.name, w -> "#y" + w)
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

        final AbstractMonster monster = PCLJUtils.SafeCast(owner, AbstractMonster.class);
        if (monster != null && (isAttacking || isDefending || isDebuffing || isBuffing))
        {
            this.moveByte = monster.nextMove;
            this.moveIntent = monster.intent;
            PCLActions.Last.Callback(() -> {
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
                    PCLJUtils.LogWarning(this, "Failed to create intent");
                }
            });
        }
    }

    public boolean DoActions() {
        if (damage > 0) {
            PCLActions.Bottom.DealDamage(owner, PCLGameUtilities.GetRandomEnemy(true), damage, DamageInfo.DamageType.NORMAL, AttackEffects.BLUNT_HEAVY);
        }
        if (block > 0) {
            PCLActions.Bottom.StackPower(new NextTurnBlockPower(player, block));
        }
        for (PCLAffinity af : affinityPowers) {
            PCLActions.Bottom.StackAffinityPower(af, AFFINITY_GAIN, false);
        }
        for (PCLPowerHelper ph : buffs) {
            PCLActions.Bottom.ApplyPower(TargetHelper.Player(), ph, BUFF_DEBUFF_GAIN);
        }
        for (PCLPowerHelper ph : debuffs) {
            PCLActions.Bottom.ApplyPower(TargetHelper.RandomEnemy(), ph, BUFF_DEBUFF_GAIN);
        }
        for (PCLOrbHelper o : orbs) {
            PCLActions.Bottom.ChannelOrbs(o, 1);
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
        int rarityModifier = PCLGameUtilities.IsHighCost(card) ? 2 : 1;
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
                    this.triggerCondition.requiredAmount = 4 + debuffs.size() + buffs.size() + affinityPowers.size() + block / 15 + (int) PCLJUtils.Sum(orbs, o -> Float.valueOf(PCLOrbHelper.COMMON_THRESHOLD - o.weight));
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

        PCLCardAffinities cardAffinities = PCLGameUtilities.GetPCLAffinities(card);
        if (cardAffinities != null) {
            PCLAffinity target = cardAffinities.GetLevel(PCLAffinity.General) <= 0 || cardAffinities.GetLevel(PCLAffinity.Star) > 0 ? PCLAffinity.General : PCLJUtils.FindMax(cardAffinities.List, af -> af.level).type;
            this.triggerCondition.affinities = new PCLAffinity[] {target};
        }

    }

    protected int GetBaseDamage() {
        final AbstractMonster monster = PCLJUtils.SafeCast(owner, AbstractMonster.class);
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
                PCLJUtils.LogWarning(this, "Monster damage could not be obtained");
            }
        }
        return 1;
    }



    @Override
    public void OnDamageAction(AbstractGameAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        if (active) {
            boolean isOwner = action.source.powers.contains(this);
            if (isOwner && canRedirect) {
                AbstractCreature newT = PCLGameUtilities.GetRandomEnemy(true);
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
                AbstractCreature newT = PCLGameUtilities.GetRandomEnemy(true);
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
}