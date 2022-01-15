package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.special.SelectCreature;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.common.EnragePower;
import pinacolada.powers.replacement.PCLCurlUpPower;
import pinacolada.powers.replacement.PlayerFlightPower;
import pinacolada.powers.special.StolenGoldPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;

public class Megaman extends PCLCard
{
    private static class MegamanMove {
        private SelectCreature.Targeting target;
        private String text;
        private ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action;
        private int uses;

        public MegamanMove(SelectCreature.Targeting target, String text, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action, int uses) {
            this.target = target;
            this.text = text;
            this.action = action;
            this.uses = uses;
        }
    }
    private static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    public static final HashMap<String, MegamanMove> EFFECT_MAP = new HashMap<>();
    public static final PCLCardData DATA = Register(Megaman.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged, EYBCardTarget.None)
            .SetMultiformData(3, false).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Megaman);
    private static final CardEffectChoice choices = new CardEffectChoice();
    private String previewForm;

    static {
        EFFECT_MAP.put(AcidSlime_L.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(1, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), 1), 2));
        EFFECT_MAP.put(AcidSlime_M.ID, EFFECT_MAP.get(AcidSlime_L.ID));
        EFFECT_MAP.put(AcidSlime_S.ID, EFFECT_MAP.get(AcidSlime_L.ID));
        EFFECT_MAP.put(BanditBear.ID, new MegamanMove(SelectCreature.Targeting.AoE,ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), 3), 1));
        EFFECT_MAP.put(BanditLeader.ID, new MegamanMove(SelectCreature.Targeting.AoE,ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), 3), 1));
        EFFECT_MAP.put(BanditPointy.ID, new MegamanMove(SelectCreature.Targeting.AoE,ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), 3), 1));
        EFFECT_MAP.put(Byrd.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(1, GR.Tooltips.Flight, true), (c, p, m) -> PCLActions.Bottom.StackPower(new PlayerFlightPower(p, 1)), 1));
        EFFECT_MAP.put(Cultist.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(1, GR.Tooltips.Ritual, true), (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Ritual, 1), 1));
        EFFECT_MAP.put(FungiBeast.ID, new MegamanMove(SelectCreature.Targeting.AoE, ACTIONS.ApplyToALL(1, GR.Tooltips.Vulnerable, true), (c, p, m) -> PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1), 2));
        EFFECT_MAP.put(GremlinFat.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), 1), 2));
        EFFECT_MAP.put(GremlinNob.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(1, GR.Tooltips.Enrage, true), (c, p, m) -> PCLActions.Bottom.StackPower(new EnragePower(p, 2)), 1));
        EFFECT_MAP.put(GremlinThief.ID, new MegamanMove(SelectCreature.Targeting.Random, ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 2), 2));
        EFFECT_MAP.put(GremlinTsundere.ID, new MegamanMove(SelectCreature.Targeting.Player,ACTIONS.GainAmount(1, GR.Tooltips.Might, true), (c, p, m) -> PCLActions.Bottom.GainMight(1), 2));
        EFFECT_MAP.put(GremlinWarrior.ID, new MegamanMove(SelectCreature.Targeting.Player,ACTIONS.GainAmount(1, GR.Tooltips.Might, true), (c, p, m) -> PCLActions.Bottom.GainMight(1), 2));
        EFFECT_MAP.put(GremlinWizard.ID, new MegamanMove(SelectCreature.Targeting.Player,ACTIONS.Channel(2, GR.Tooltips.Dark, true), (c, p, m) -> PCLActions.Bottom.ChannelOrbs(Dark::new, 2), 1));
        EFFECT_MAP.put(JawWorm.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(5, GR.Tooltips.Block, true), (c, p, m) -> PCLActions.Bottom.GainBlock(5), 2));
        EFFECT_MAP.put(Lagavulin.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(2, GR.Tooltips.Metallicize, true), (c, p, m) -> PCLActions.Bottom.GainMetallicize(2), 1));
        EFFECT_MAP.put(LouseDefensive.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(5, GR.Tooltips.CurlUp, true), (c, p, m) -> PCLActions.Bottom.StackPower(new PCLCurlUpPower(p, 5)), 2));
        EFFECT_MAP.put(LouseNormal.ID, EFFECT_MAP.get(LouseNormal.ID));
        EFFECT_MAP.put(Mugger.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Steal(5, GR.Tooltips.Gold, true), (c, p, m) -> PCLActions.Bottom.StackPower(new StolenGoldPower(m, 5)), 2));
        EFFECT_MAP.put(Sentry.ID, new MegamanMove(SelectCreature.Targeting.Player, ACTIONS.GainAmount(2, GR.Tooltips.Metallicize, true), (c, p, m) -> PCLActions.Bottom.GainMetallicize(2), 1));
        EFFECT_MAP.put(SlaverBlue.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(3, GR.Tooltips.Shackles, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Normal(m), 3), 1));
        EFFECT_MAP.put(SlaverRed.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(3, GR.Tooltips.Shackles, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Normal(m), 3), 1));
        EFFECT_MAP.put(SpikeSlime_L.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(1, GR.Tooltips.Frail, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), 1), 2));
        EFFECT_MAP.put(SpikeSlime_M.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
        EFFECT_MAP.put(SpikeSlime_S.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
    }

    public Megaman()
    {
        super(DATA);

        Initialize(7, 0, 3, 3);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Silver(1, 0, 3);
        SetAffinity_Light(1);

        SetUnique(true, -1);

        SetCooldown(1, 0, __ -> {});
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form],
                auxiliaryData.additionalData.isEmpty() ? GetStealString() : GetEquippedString());
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);
        if (m != null)
        {
            previewForm = m.id;
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (cooldown.GetCurrent() == 0) {
            switch (auxiliaryData.form) {
                case 1:
                    SetAttackType(PCLAttackType.Fire);
                    break;
                case 2:
                    SetAttackType(PCLAttackType.Ice);
                    break;
                default:
                    SetAttackType(PCLAttackType.Electric);
                    break;
            }
        }
        else {
            SetAttackType(PCLAttackType.Ranged);
        }
    }

    @Override
    protected float GetInitialDamage()
    {
        if (cooldown.GetCurrent() == 0) {
            return super.GetInitialDamage() * secondaryValue;
        }
        return super.GetInitialDamage();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        MegamanMove currentMove = GetCurrentMove();
        PCLActions.Bottom.SelectCreature(SelectCreature.Targeting.Any, this.name).IsCancellable(false).AddCallback((c) -> {
            if (c.isPlayer) {
                cooldown.ProgressCooldown(1);
            } else {
                PCLActions.Bottom.DealCardDamage(this, c, attackType == PCLAttackType.Ranged ? AttackEffects.GUNSHOT : AttackEffects.PSYCHOKINESIS)
                        .forEach(d -> d.SetVFX(true, false)
                                .SetVFXColor(GetVFXColor(), GetVFXColor())
                                .SetDamageEffect(enemy ->
                                        attackType == PCLAttackType.Ranged ? 0.1f : PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, GetVFXColor())).duration * 0.1f)
                                .AddCallback(enemy -> {
                                    if (PCLGameUtilities.IsFatal(enemy, false) && auxiliaryData.additionalData.isEmpty())
                                    {
                                        PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
                                        PCLActions.Bottom.ModifyAllInstances(uuid, card -> {
                                                    Megaman mg = PCLJUtils.SafeCast(card, Megaman.class);
                                                    if (mg != null) {
                                                        mg.auxiliaryData.additionalData.add(enemy.id);
                                                        mg.misc = mg.GetCurrentMove().uses;
                                                    }
                                                })
                                                .IncludeMasterDeck(true)
                                                .IsCancellable(false);
                                    }
                                })
                        );
            }
        });

        if (currentMove != null) {
            choices.Initialize(this, true);
            choices.AddEffect(new GenericEffect_Megaman(currentMove));
            choices.Select(1, m).SetOptions(false, true).AddCallback(cards -> {
                if (cards.size() > 0) {
                    PCLActions.Bottom.ModifyAllInstances(uuid, card -> {
                                Megaman mg = PCLJUtils.SafeCast(card, Megaman.class);
                                if (mg != null) {
                                    mg.misc -= 1;
                                    if (mg.misc <= 0) {
                                        mg.auxiliaryData.additionalData.clear();
                                    }
                                }
                            })
                            .IncludeMasterDeck(true)
                            .IsCancellable(false);
                }
            });
        }
    }

    protected MegamanMove GetCurrentMove() {
        return auxiliaryData.additionalData.size() > 0 ? GetMove(auxiliaryData.additionalData.get(0)) : null;
    }

    protected MegamanMove GetMove(String monsterID) {
        return EFFECT_MAP.getOrDefault(monsterID != null ? monsterID : "", EFFECT_MAP.get(AcidSlime_S.ID));
    }

    protected String GetEquippedString() {
        return PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[6], GetCurrentMove().text, misc);
    }

    protected String GetStealString() {
        return PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[3], previewForm != null ?
                PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[5], GetMove(previewForm).uses, GetMove(previewForm).text)
        : cardData.Strings.EXTENDED_DESCRIPTION[4]);
    }

    protected Color GetVFXColor() {
        switch (attackType) {
            case Fire:
                return Color.FIREBRICK;
            case Ice:
                return Color.SKY;
            case Electric:
                return Color.GOLDENROD;
            default:
                return Color.WHITE;
        }
    }

    protected static class GenericEffect_Megaman extends GenericEffect
    {
        protected final MegamanMove move;

        public GenericEffect_Megaman(MegamanMove move)
        {
            this.move = move;
        }

        @Override
        public String GetText()
        {
            return move.text;
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            move.action.Invoke(card, p, m);
        }
    }
}