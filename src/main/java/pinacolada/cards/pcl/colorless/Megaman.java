package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.beyond.Spiker;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.ListSelection;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Megaman extends PCLCard
{
    private static class MegamanMove {
        private final SelectCreature.Targeting target;
        private final String text;
        private final ActionT3<PCLCard, AbstractPlayer, AbstractCreature> action;
        private final int uses;

        public MegamanMove(SelectCreature.Targeting target, String text, ActionT3<PCLCard, AbstractPlayer, AbstractCreature> action, int uses) {
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
        EFFECT_MAP.put(BanditBear.ID, new MegamanMove(SelectCreature.Targeting.AoE,ACTIONS.ApplyToALL(3, GR.Tooltips.Frail, true), (c, p, m) -> PCLActions.Bottom.ApplyFrail(TargetHelper.Enemies(), 3), 1));
        EFFECT_MAP.put(BanditLeader.ID, new MegamanMove(SelectCreature.Targeting.AoE,ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 3), 1));
        EFFECT_MAP.put(BanditPointy.ID, new MegamanMove(SelectCreature.Targeting.Enemy,ACTIONS.DealDamage(14, true), (c, p, m) -> PCLActions.Bottom.DealDamage(p, m, 14, DamageInfo.DamageType.NORMAL, AttackEffects.SLASH_HORIZONTAL), 1));
        EFFECT_MAP.put(BookOfStabbing.ID, new MegamanMove(SelectCreature.Targeting.Enemy,ACTIONS.DealDamage(24, true), (c, p, m) -> PCLActions.Bottom.DealDamage(p, m, 18, DamageInfo.DamageType.NORMAL, AttackEffects.SLASH_DIAGONAL), 1));
        EFFECT_MAP.put(Byrd.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(1, GR.Tooltips.Flight, true), (c, p, m) -> PCLActions.Bottom.StackPower(new PlayerFlightPower(p, 1)), 1));
        EFFECT_MAP.put(Centurion.ID, new MegamanMove(SelectCreature.Targeting.None,ACTIONS.GainAmount(10, GR.Tooltips.Block, true), (c, p, m) -> PCLActions.Bottom.GainBlock(10), 1));
        EFFECT_MAP.put(Champ.ID,
                new MegamanMove(SelectCreature.Targeting.None, ACTIONS.RemoveCommonDebuffs(true),
                        (c, p, m) -> {
                            PCLActions.Bottom.RemoveCommonDebuffs(player, ListSelection.First(0), player.powers.size());
                    }, 1));
        EFFECT_MAP.put(Cultist.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(1, GR.Tooltips.Ritual, true), (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Ritual, 1), 1));
        EFFECT_MAP.put(Exploder.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(15, GR.Tooltips.DelayedDamage, true), (c, p, m) -> PCLActions.Bottom.DealDamageAtEndOfTurn(p, m, 15), 2));
        EFFECT_MAP.put(FungiBeast.ID, new MegamanMove(SelectCreature.Targeting.AoE, ACTIONS.ApplyToALL(1, GR.Tooltips.Vulnerable, true), (c, p, m) -> PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1), 2));
        EFFECT_MAP.put(GremlinFat.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.ApplyToRandom(2, GR.Tooltips.Weak, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), 1), 2));
        EFFECT_MAP.put(GremlinLeader.ID, new MegamanMove(SelectCreature.Targeting.Enemy,ACTIONS.DealDamage(18, true), (c, p, m) -> PCLActions.Bottom.DealDamage(p, m, 18, DamageInfo.DamageType.NORMAL, AttackEffects.SLASH_DIAGONAL), 1));
        EFFECT_MAP.put(GremlinNob.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(1, GR.Tooltips.Enrage, true), (c, p, m) -> PCLActions.Bottom.StackPower(new EnragePower(p, 2)), 1));
        EFFECT_MAP.put(GremlinThief.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.DealDamage(8, true), (c, p, m) -> PCLActions.Bottom.DealDamage(p, m, 8, DamageInfo.DamageType.NORMAL, AttackEffects.SLASH_VERTICAL), 2));
        EFFECT_MAP.put(GremlinTsundere.ID, new MegamanMove(SelectCreature.Targeting.None,ACTIONS.GainAmount(9, GR.Tooltips.Block, true), (c, p, m) -> PCLActions.Bottom.GainBlock(9), 1));
        EFFECT_MAP.put(GremlinWarrior.ID, new MegamanMove(SelectCreature.Targeting.None,ACTIONS.GainAmount(1, GR.Tooltips.Might, true), (c, p, m) -> PCLActions.Bottom.GainMight(1), 2));
        EFFECT_MAP.put(GremlinWizard.ID, new MegamanMove(SelectCreature.Targeting.None,ACTIONS.Channel(2, GR.Tooltips.Dark, true), (c, p, m) -> PCLActions.Bottom.ChannelOrbs(Dark::new, 2), 1));
        EFFECT_MAP.put(Healer.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.HealHP(4, true), (c, p, m) -> PCLActions.Bottom.Heal(4), 2));
        EFFECT_MAP.put(Hexaghost.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(10, GR.Tooltips.Burning, true), (c, p, m) -> PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(m), 10), 1));
        EFFECT_MAP.put(JawWorm.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(6, GR.Tooltips.Block, true), (c, p, m) -> PCLActions.Bottom.GainBlock(6), 2));
        EFFECT_MAP.put(Lagavulin.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(2, GR.Tooltips.Metallicize, true), (c, p, m) -> PCLActions.Bottom.GainMetallicize(2), 1));
        EFFECT_MAP.put(LouseDefensive.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(5, GR.Tooltips.CurlUp, true), (c, p, m) -> PCLActions.Bottom.StackPower(new PCLCurlUpPower(p, 5)), 2));
        EFFECT_MAP.put(LouseNormal.ID, EFFECT_MAP.get(LouseNormal.ID));
        EFFECT_MAP.put(Mugger.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Steal(5, GR.Tooltips.Gold, true), (c, p, m) -> PCLActions.Bottom.StackPower(new StolenGoldPower(m, 5)), 2));
        EFFECT_MAP.put(OrbWalker.ID, new MegamanMove(SelectCreature.Targeting.None,ACTIONS.GainAmount(1, GR.Tooltips.Wisdom, true), (c, p, m) -> PCLActions.Bottom.GainWisdom(1), 2));
        EFFECT_MAP.put(Repulsor.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.Draw(1, true), (c, p, m) -> PCLActions.Bottom.Draw(1), 3));
        EFFECT_MAP.put(ShelledParasite.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(2, GR.Tooltips.PlatedArmor, true), (c, p, m) -> PCLActions.Bottom.GainPlatedArmor(2), 2));
        EFFECT_MAP.put(Sentry.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.Draw(2, true), (c, p, m) -> PCLActions.Bottom.Draw(2), 2));
        EFFECT_MAP.put(SlaverBlue.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(3, GR.Tooltips.Shackles, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Normal(m), 3), 1));
        EFFECT_MAP.put(SlaverRed.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(3, GR.Tooltips.Shackles, true), (c, p, m) -> PCLActions.Bottom.ApplyShackles(TargetHelper.Normal(m), 3), 1));
        EFFECT_MAP.put(SlimeBoss.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(38, GR.Tooltips.DelayedDamage, true), (c, p, m) -> PCLActions.Bottom.DealDamageAtEndOfTurn(p, m, 38), 1));
        EFFECT_MAP.put(SnakePlant.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(3, GR.Tooltips.Malleable, true), (c, p, m) -> PCLActions.Bottom.GainMalleable(3), 1));
        EFFECT_MAP.put(SphericGuardian.ID, new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(3, GR.Tooltips.Blur, true), (c, p, m) -> PCLActions.Bottom.GainBlur(3), 1));
        EFFECT_MAP.put(SpikeSlime_L.ID, new MegamanMove(SelectCreature.Targeting.Enemy, ACTIONS.Apply(1, GR.Tooltips.Frail, true), (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), 1), 2));
        EFFECT_MAP.put(SpikeSlime_M.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
        EFFECT_MAP.put(SpikeSlime_S.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
        EFFECT_MAP.put(Spiker.ID, new MegamanMove(SelectCreature.Targeting.None,ACTIONS.GainAmount(1, GR.Tooltips.Thorns, true), (c, p, m) -> PCLActions.Bottom.GainThorns(1), 2));
        EFFECT_MAP.put(TheCollector.ID,
                new MegamanMove(SelectCreature.Targeting.AoE,ACTIONS.ApplyToALL(3, GR.Tooltips.Frail + " " + GR.Tooltips.Vulnerable + " " + GR.Tooltips.Weak, true),
                        (c, p, m) -> {
                            PCLActions.Bottom.ApplyFrail(TargetHelper.Enemies(), 3);
                            PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 3);
                            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 3);
                        }, 1));
        EFFECT_MAP.put(TheGuardian.ID,
                new MegamanMove(SelectCreature.Targeting.None, ACTIONS.GainAmount(4, GR.Tooltips.Thorns, true) + " " + ACTIONS.GainAmount(20, GR.Tooltips.CurlUp, true),
                        (c, p, m) -> {
                            PCLActions.Bottom.GainThorns(4);
                            PCLActions.Bottom.StackPower(new PCLCurlUpPower(p, 20));
                        }
                        , 1));
        EFFECT_MAP.put(TorchHead.ID, EFFECT_MAP.get(GremlinThief.ID));
    }

    public Megaman()
    {
        super(DATA);

        Initialize(7, 0, 0, 3);
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
    public int GetXValue() {
        return misc;
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
                                        PCLActions.Bottom.ModifyAllInstances(uuid, card -> {
                                                    Megaman mg = PCLJUtils.SafeCast(card, Megaman.class);
                                                    if (mg != null) {
                                                        if (mg.auxiliaryData.additionalData == null) {
                                                            mg.auxiliaryData.additionalData = new ArrayList<>();
                                                        }
                                                        mg.auxiliaryData.additionalData.add(enemy.id);
                                                        mg.misc = mg.GetCurrentMove().uses;
                                                        mg.initializeDescription();
                                                    }
                                                })
                                                .IncludeMasterDeck(true)
                                                .AddCallback(() -> {
                                                    PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
                                                })
                                                .IsCancellable(false);
                                    }
                                    cooldown.ResetCooldown();
                                })
                        );
            }

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
                                                    mg.initializeDescription();
                                                }
                                            }
                                        })
                                        .IncludeMasterDeck(true)
                                        .IsCancellable(false);
                            }
                        });
                    }
        }
        );
    }

    protected MegamanMove GetCurrentMove() {
        return auxiliaryData.additionalData.size() > 0 ? GetMove(auxiliaryData.additionalData.get(0)) : null;
    }

    protected MegamanMove GetMove(String monsterID) {
        return EFFECT_MAP.getOrDefault(monsterID != null ? monsterID : "", EFFECT_MAP.get(Sentry.ID));
    }

    protected String GetEquippedString() {
        return PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[6], GetCurrentMove().text, misc, GetCurrentMove().uses);
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
            PCLActions.Bottom.SelectCreature(move.target, "").IsCancellable(false).AddCallback(c -> {
                move.action.Invoke(card, p, c);
            });
        }
    }
}