package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.potions.SneckoOil;
import eatyourbeets.utilities.ListSelection;
import pinacolada.actions.special.SelectCreature;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.CompositeEffect;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
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
        private final GenericEffect effect;
        private final int uses;

        public MegamanMove(int uses, GenericEffect... genericEffects) {
            this.uses = uses;
            if (genericEffects.length == 1) {
                effect = genericEffects[0];
            }
            else {
                effect = new CompositeEffect(genericEffects);
            }
        }
    }
    private static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    public static final HashMap<String, MegamanMove> EFFECT_MAP = new HashMap<>();
    public static final PCLCardData DATA = Register(Megaman.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged, PCLCardTarget.None)
            .SetMultiformData(3, false).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Megaman);
    private static final CardEffectChoice choices = new CardEffectChoice();
    private String previewForm;

    static {
        EFFECT_MAP.put(AcidSlime_L.ID, new MegamanMove(2, GenericEffect.Apply(1, PCLPowerHelper.Weak)));
        EFFECT_MAP.put(AcidSlime_M.ID, EFFECT_MAP.get(AcidSlime_L.ID));
        EFFECT_MAP.put(AcidSlime_S.ID, EFFECT_MAP.get(AcidSlime_L.ID));
        EFFECT_MAP.put(BanditBear.ID, new MegamanMove(1, GenericEffect.ApplyToEnemies(3, PCLPowerHelper.Frail)));
        EFFECT_MAP.put(BanditLeader.ID, new MegamanMove(1, GenericEffect.ApplyToEnemies(3, PCLPowerHelper.Weak)));
        EFFECT_MAP.put(BanditPointy.ID, new MegamanMove(1, GenericEffect.DealDamage(15, AttackEffects.SLASH_DIAGONAL)));
        EFFECT_MAP.put(BookOfStabbing.ID, new MegamanMove(1, GenericEffect.DealDamageToAll(12, AttackEffects.SLASH_DIAGONAL)));
        EFFECT_MAP.put(BronzeAutomaton.ID, new MegamanMove(1, GenericEffect.DealDamageToAll(36, AttackEffects.LIGHTNING), GenericEffect.Gain(-7, PCLPowerHelper.Focus)));
        EFFECT_MAP.put(Byrd.ID, new MegamanMove(1, GenericEffect.Gain(1, PCLPowerHelper.Flight)));
        EFFECT_MAP.put(Centurion.ID, new MegamanMove(1, GenericEffect.GainBlock(10)));
        EFFECT_MAP.put(Champ.ID, new MegamanMove(1, new GenericEffect_MegamanChamp()));
        EFFECT_MAP.put(Chosen.ID, new MegamanMove(1, GenericEffect.Apply(2, PCLPowerHelper.Weak), GenericEffect.GainAffinityPower(2, PCLAffinity.Red)));
        EFFECT_MAP.put(Cultist.ID, new MegamanMove(1, GenericEffect.Gain(1, PCLPowerHelper.Ritual)));
        EFFECT_MAP.put(Exploder.ID, new MegamanMove(1, GenericEffect.ApplyToEnemies(14, PCLPowerHelper.DelayedDamage)));
        EFFECT_MAP.put(FungiBeast.ID, new MegamanMove(1, GenericEffect.ApplyToEnemies(1, PCLPowerHelper.Vulnerable)));
        EFFECT_MAP.put(GiantHead.ID, new MegamanMove(1, GenericEffect.Apply(1, PCLPowerHelper.Slow)));
        EFFECT_MAP.put(GremlinFat.ID, new MegamanMove(2, GenericEffect.ApplyToRandom(2, PCLPowerHelper.Weak)));
        EFFECT_MAP.put(GremlinLeader.ID, EFFECT_MAP.get(BanditPointy.ID));
        EFFECT_MAP.put(GremlinNob.ID, new MegamanMove(1, GenericEffect.Gain(2, PCLPowerHelper.Enrage)));
        EFFECT_MAP.put(GremlinThief.ID, new MegamanMove(2, GenericEffect.DealDamage(9, AttackEffects.SLASH_HORIZONTAL)));
        EFFECT_MAP.put(GremlinTsundere.ID, new MegamanMove(1, GenericEffect.GainBlock(9)));
        EFFECT_MAP.put(GremlinWarrior.ID, new MegamanMove(1, GenericEffect.GainAffinityPower(2, PCLAffinity.Red)));
        EFFECT_MAP.put(GremlinWizard.ID, new MegamanMove(1, GenericEffect.ChannelOrb(2, PCLOrbHelper.Dark)));
        EFFECT_MAP.put(Healer.ID, new MegamanMove(1, GenericEffect.Gain(1, PCLPowerHelper.Regen)));
        EFFECT_MAP.put(Hexaghost.ID, new MegamanMove(1, GenericEffect.Gain(3, PCLPowerHelper.BurningWeapon), GenericEffect.ApplyToEnemies(3, PCLPowerHelper.Burning)));
        EFFECT_MAP.put(JawWorm.ID, new MegamanMove(2, GenericEffect.GainBlock(3), GenericEffect.DealDamage(3, AttackEffects.BLUNT_LIGHT)));
        EFFECT_MAP.put(Lagavulin.ID, new MegamanMove(1, GenericEffect.Gain(2, PCLPowerHelper.Metallicize)));
        EFFECT_MAP.put(Looter.ID, new MegamanMove(1, new GenericEffect_MegamanSteal(7)));
        EFFECT_MAP.put(LouseDefensive.ID, new MegamanMove(2, GenericEffect.Gain(5, PCLPowerHelper.CurlUp)));
        EFFECT_MAP.put(LouseNormal.ID, EFFECT_MAP.get(LouseNormal.ID));
        EFFECT_MAP.put(Mugger.ID, new MegamanMove(2, new GenericEffect_MegamanSteal(5)));
        EFFECT_MAP.put(Nemesis.ID, new MegamanMove(1, GenericEffect.Gain(1, PCLPowerHelper.Intangible)));
        EFFECT_MAP.put(OrbWalker.ID, new MegamanMove(1, GenericEffect.GainAffinityPower(2, PCLAffinity.Blue)));
        EFFECT_MAP.put(Repulsor.ID, new MegamanMove(3, GenericEffect.Draw(1)));
        EFFECT_MAP.put(Reptomancer.ID, new MegamanMove(1, GenericEffect.Obtain(3, 0, ThrowingKnife.DATA)));
        EFFECT_MAP.put(ShelledParasite.ID, new MegamanMove(2, GenericEffect.Gain(2, PCLPowerHelper.PlatedArmor)));
        EFFECT_MAP.put(Sentry.ID, new MegamanMove(2, GenericEffect.Gain(1, PCLPowerHelper.Artifact)));
        EFFECT_MAP.put(SlaverBlue.ID, new MegamanMove(2, GenericEffect.Apply(3, PCLPowerHelper.Shackles)));
        EFFECT_MAP.put(SlaverRed.ID, new MegamanMove(1, GenericEffect.Apply(3, PCLPowerHelper.Vulnerable)));
        EFFECT_MAP.put(SlimeBoss.ID, new MegamanMove(1, GenericEffect.ApplyToRandom(38, PCLPowerHelper.DelayedDamage)));
        EFFECT_MAP.put(SnakeDagger.ID, new MegamanMove(2, GenericEffect.Obtain(1, 0, ThrowingKnife.DATA)));
        EFFECT_MAP.put(SnakePlant.ID, new MegamanMove(1, GenericEffect.Gain(3, PCLPowerHelper.Malleable)));
        EFFECT_MAP.put(Snecko.ID, new MegamanMove(1, new GenericEffect_MegamanSnecko()));
        EFFECT_MAP.put(SphericGuardian.ID, new MegamanMove(2, GenericEffect.Gain(1, PCLPowerHelper.Blur)));
        EFFECT_MAP.put(SpikeSlime_L.ID, new MegamanMove(2, GenericEffect.Apply(1, PCLPowerHelper.Frail)));
        EFFECT_MAP.put(SpikeSlime_M.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
        EFFECT_MAP.put(SpikeSlime_S.ID, EFFECT_MAP.get(SpikeSlime_L.ID));
        EFFECT_MAP.put(Spiker.ID, new MegamanMove(1, GenericEffect.Gain(3, PCLPowerHelper.Thorns)));
        EFFECT_MAP.put(Taskmaster.ID, new MegamanMove(1, GenericEffect.DealDamage(9, AttackEffects.SLASH_DIAGONAL), GenericEffect.Apply(1, PCLPowerHelper.Vulnerable)));
        EFFECT_MAP.put(TimeEater.ID, new MegamanMove(1, GenericEffect.Obtain(HououinKyouma.DATA)));
        EFFECT_MAP.put(TheCollector.ID, new MegamanMove(1, GenericEffect.ApplyToEnemies(3, PCLPowerHelper.Vulnerable, PCLPowerHelper.Weak, PCLPowerHelper.Frail)));
        EFFECT_MAP.put(TheGuardian.ID, new MegamanMove(1, GenericEffect.Gain(5, PCLPowerHelper.Thorns), GenericEffect.Gain(20, PCLPowerHelper.CurlUp)));
        EFFECT_MAP.put(TorchHead.ID, EFFECT_MAP.get(GremlinThief.ID));
        EFFECT_MAP.put(Transient.ID, new MegamanMove(1, GenericEffect.Apply(99, PCLPowerHelper.Shackles)));
        EFFECT_MAP.put(WrithingMass.ID, new MegamanMove(1, GenericEffect.Gain(1, PCLPowerHelper.Malleable), GenericEffect.Apply(1, PCLPowerHelper.Weak, PCLPowerHelper.Vulnerable)));
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
                        choices.AddEffect(currentMove.effect);
                        choices.SelectWithTargeting(1).SetOptions(false, true).AddCallback(cards -> {
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
        return PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[6], GetCurrentMove().effect.GetText(), misc, GetCurrentMove().uses);
    }

    protected String GetStealString() {
        return PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[3], previewForm != null ?
                PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[5], GetMove(previewForm).uses, GetMove(previewForm).effect.GetText())
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

    public static class GenericEffect_MegamanChamp extends GenericEffect
    {
        public GenericEffect_MegamanChamp()
        {
        }

        @Override
        public String GetText() {
            return ACTIONS.RemoveCommonDebuffs(true);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.RemoveCommonDebuffs(player, ListSelection.First(0), player.powers.size());
        }
    }

    public static class GenericEffect_MegamanSnecko extends GenericEffect
    {
        protected static PotionStrings strings;

        public GenericEffect_MegamanSnecko()
        {
        }

        @Override
        public String GetText() {
            if (strings == null) {
                strings = CardCrawlGame.languagePack.getPotionString(SneckoOil.POTION_ID);
            }
            return ACTIONS.Use(strings != null ? strings.NAME : SneckoOil.POTION_ID, true);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.UsePotion(new SneckoOil(), m).SetShouldRemove(false);
        }
    }

    public static class GenericEffect_MegamanSteal extends GenericEffect
    {
        public GenericEffect_MegamanSteal(int amount)
        {
            super(null, null, PCLCardTarget.Normal, amount);
        }

        @Override
        public String GetText() {
            return ACTIONS.Steal(amount, GR.Tooltips.Gold, true);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.StackPower(new StolenGoldPower(m, amount));
        }
    }
}