package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.colorless.Yoimiya;
import pinacolada.cards.pcl.special.Ganyu;
import pinacolada.cards.pcl.special.Traveler_Wish;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.cards.pcl.tokens.AffinityToken_Dark;
import pinacolada.cards.pcl.ultrarare.Dainsleif;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PowerHelper;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.powers.replacement.TemporaryDrawReductionPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DainsleifAbyssPower extends PCLPower {
    private final static HashMap<Integer, RandomizedList<AbyssNegativeEffect>> negativeEffectList = new HashMap<>();
    private final static RandomizedList<AbstractCard> genshinCards = new RandomizedList<>();
    private final static WeightedList<AbyssPositiveEffect> positiveEffectList = new WeightedList<>();
    private static final PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    private static final String NAME = Dainsleif.DATA.Strings.NAME;
    public static final int COUNTDOWN_AMT = 24;
    public static final String POWER_ID = CreateFullID(DainsleifAbyssPower.class);
    public static final int CHOICES = 3;


    public DainsleifAbyssPower(AbstractCreature owner) {
        super(owner, POWER_ID);

        this.amount = 0;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = FormatDescription(0, COUNTDOWN_AMT);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        this.amount += 1 + card.cost;
        if (this.amount >= COUNTDOWN_AMT) {
            this.amount -= COUNTDOWN_AMT;
            this.playApplyPowerSfx();
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            PCLActions.Bottom.VFX(new TimeWarpTurnEndEffect());
            PCLActions.Bottom.VFX(new BorderFlashEffect(Color.BLUE, true));
            PCLActions.Top.PlayFromPile(null, 1, null, player.drawPile, player.discardPile, player.hand).SetOptions(true, false).SetFilter(ca -> ca instanceof Dainsleif);
            ChooseEffect();
            PCLActions.Last.Add(new PressEndTurnButtonAction());
        }
        this.updateDescription();
    }

    private void InitializeLists() {
        if (genshinCards.Size() == 0) {
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c instanceof PCLCard && ((PCLCard) c).series != null && ((PCLCard) c).series.Equals(CardSeries.GenshinImpact) &&
                        (c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE)) {
                    genshinCards.Add(c);
                }
            }
            genshinCards.Add(new Ganyu());
            genshinCards.Add(new Yoimiya());
        }

        if (negativeEffectList.size() == 0) {
            for (AbyssNegativeEffect effect : AbyssNegativeEffect.class.getEnumConstants()) {
                RandomizedList<AbyssNegativeEffect> list = negativeEffectList.getOrDefault(effect.tier, new RandomizedList<>());
                if (!negativeEffectList.containsKey(effect.tier)) {
                    negativeEffectList.put(effect.tier, list);
                }
                list.Add(effect);
            }
        }
        if (positiveEffectList.Size() == 0) {
            for (AbyssPositiveEffect effect : AbyssPositiveEffect.class.getEnumConstants()) {
                positiveEffectList.Add(effect, effect.weight);
            }
        }
    }

    private void ChooseEffect() {
        InitializeLists();

        WeightedList<AbyssPositiveEffect> tempPositiveEffects = new WeightedList<>(positiveEffectList);
        HashMap<Integer, RandomizedList<AbyssNegativeEffect>> tempNegativeEffects = new HashMap<>();
        for (Map.Entry<Integer,RandomizedList<AbyssNegativeEffect>> entry : negativeEffectList.entrySet()) {
            tempNegativeEffects.put(entry.getKey(), new RandomizedList<>(entry.getValue().GetInnerList()));
        }

        ArrayList<PCLCard_Dynamic> currentEffects = new ArrayList<>();

        while (currentEffects.size() < CHOICES) {
            AbyssPositiveEffect pEffect = tempPositiveEffects.Retrieve(rng);
            AbyssNegativeEffect nEffect = tempNegativeEffects.getOrDefault(pEffect.tier, new RandomizedList<>()).Retrieve(rng);
            if (nEffect != null) {
                PCLCardBuilder builder = Generate(pEffect, nEffect);
                currentEffects.add(builder.Build());
            }
            if (tempPositiveEffects.Size() == 0) {
                break;
            }
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (PCLCard_Dynamic card : currentEffects) {
            if (card != null) {
                card.calculateCardDamage(null);
                group.addToTop(card);
            }
        }

        PCLActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (!cards.isEmpty()) {
                        cards.get(0).use(player, null);
                    }
                });
    }

    public PCLCardBuilder Generate(AbyssPositiveEffect positiveEffect, AbyssNegativeEffect negativeEffect) {
        PCLCardBuilder builder = new PCLCardBuilder(Dainsleif.DATA.ID + "Alt");
        String combinedText = positiveEffect.text + " NL " + negativeEffect.text;

        builder.SetText(NAME, combinedText, "");
        builder.SetProperties(AbstractCard.CardType.SKILL, GR.PCL.CardColor, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ALL);
        builder.SetOnUse((c, p, m) -> {
            positiveEffect.action.Invoke(c, p, m);
            negativeEffect.action.Invoke(c, p, m);
        });

        return builder;
    }


    private enum AbyssNegativeEffect {
        DrawLessNextTurn(ACTIONS.NextTurnDrawLess(1, true), 1, (c, p, m) -> PCLActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1))),
        EnemiesGainPlatedArmor(ACTIONS.GiveAllEnemies(1, GR.Tooltips.PlatedArmor, true), 1, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.PlatedArmor, 1)),
        EnemiesGainStrength(ACTIONS.GiveAllEnemies(2, GR.Tooltips.Strength, true), 2, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Strength, 2)),
        EnemiesGainStrength2(ACTIONS.GiveAllEnemies(4, GR.Tooltips.Strength, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Strength, 4)),
        EnemiesGainThorns(ACTIONS.GiveAllEnemies(5, GR.Tooltips.Thorns, true), 2, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Thorns, 2)),
        PlayerGainBurning(ACTIONS.GainAmount(4, GR.Tooltips.Burning, true), 1, (c, p, m) -> PCLActions.Bottom.ApplyBurning(null, p, 4)),
        PlayerGainElectrified(ACTIONS.GainAmount(4, GR.Tooltips.Electrified, true), 1, (c, p, m) -> PCLActions.Bottom.ApplyElectrified(null, p, 4)),
        PlayerGainFrail(ACTIONS.GainAmount(2, GR.Tooltips.Frail, true), 1, (c, p, m) -> PCLActions.Bottom.ApplyFrail(null, p, 2)),
        PlayerGainFreezing(ACTIONS.GainAmount(4, GR.Tooltips.Freezing, true), 1, (c, p, m) -> PCLActions.Bottom.ApplyFreezing(null, p, 4)),
        PlayerGainVulnerable(ACTIONS.GainAmount(2, GR.Tooltips.Vulnerable, true), 1, (c, p, m) -> PCLActions.Bottom.ApplyVulnerable(null, p, 2)),
        PlayerGainVulnerable2(ACTIONS.GainAmount(3, GR.Tooltips.Vulnerable, true), 2, (c, p, m) -> PCLActions.Bottom.ApplyVulnerable(null, p, 3)),
        PlayerGainWeak(ACTIONS.GainAmount(2, GR.Tooltips.Weak, true), 1, (c, p, m) -> PCLActions.Bottom.ApplyWeak(null, p, 2)),
        PlayerGainWeak2(ACTIONS.GainAmount(3, GR.Tooltips.Weak, true), 2, (c, p, m) -> PCLActions.Bottom.ApplyWeak(null, p, 3)),
        PlayerLoseBalance(ACTIONS.LosePower(1, GR.Tooltips.Resistance, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Resistance, -1)),
        PlayerLoseDexterity(ACTIONS.LosePower(1, GR.Tooltips.Dexterity, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Dexterity, -1)),
        PlayerLoseFocus(ACTIONS.LosePower(1, GR.Tooltips.Focus, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Focus, -1)),
        PlayerLoseStrength(ACTIONS.LosePower(1, GR.Tooltips.Strength, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Strength, -1)),
        PlayerTakeDamage(ACTIONS.TakeDamage(5, true), 2, (c, p, m) -> PCLActions.Bottom.StackPower(new DelayedDamagePower(p, 5))),
        PlayerTakeDamage2(ACTIONS.TakeDamage(8, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(new DelayedDamagePower(p, 8))),
        RandomEnemyGainStrength(ACTIONS.GiveRandomEnemy(3, GR.Tooltips.Strength, true), 2, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Strength, 3)),
        RandomEnemyGainStrength2(ACTIONS.GiveRandomEnemy(6, GR.Tooltips.Strength, true), 3, (c, p, m) -> PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Strength, 6)),
        ObtainCurses(ACTIONS.CreateCurses(true), 4, (c, p, m) -> {
            PCLActions.Bottom.Add(new CreateRandomCurses(3, player.drawPile));
        });


        private final String text;
        private final int tier;
        private final ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action;

        AbyssNegativeEffect(String text, int tier, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action) {
            this.text = text;
            this.tier = tier;
            this.action = action;
        }
    }

    private enum AbyssPositiveEffect {
        ApplyBlinded(ACTIONS.ApplyToALL(3, GR.Tooltips.Blinded, true), 10, 1, (c, p, m) -> PCLActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), 2)),
        ApplyBurning(ACTIONS.ApplyToALL(5, GR.Tooltips.Burning, true), 10, 1, (c, p, m) -> PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), 4)),
        ApplyFreezing(ACTIONS.ApplyToALL(5, GR.Tooltips.Freezing, true), 10, 1, (c, p, m) -> PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), 4)),
        ApplyElectrified(ACTIONS.ApplyToALL(5, GR.Tooltips.Electrified, true), 10, 1, (c, p, m) -> PCLActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), 4)),
        ApplyVulnerable(ACTIONS.ApplyToALL(3, GR.Tooltips.Vulnerable, true), 10, 1, (c, p, m) -> PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 3)),
        ApplyWeak(ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), 10, 1, (c, p, m) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 3)),
        ChannelRandomOrbs(ACTIONS.ChannelRandomOrbs(2, true), 10, 2, (c, p, m) -> PCLActions.Bottom.ChannelRandomOrbs(2)),
        ChannelRandomOrbs2(ACTIONS.ChannelRandomOrbs(4, true), 10, 3, (c, p, m) -> PCLActions.Bottom.ChannelRandomOrbs(4)),
        NextTurnDraw(ACTIONS.NextTurnDraw(3, true), 10, 2, (c, p, m) -> PCLActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 3))),
        NextTurnEnergy(ACTIONS.NextTurnEnergy(2, true), 10, 2, (c, p, m) -> PCLActions.Bottom.StackPower(new EnergizedPower(p, 2))),
        GainBlessing(ACTIONS.GainAmount(6, GR.Tooltips.Invocation, true), 8, 2, (c, p, m) -> PCLActions.Bottom.GainInvocation(6, false)),
        GainCorruption(ACTIONS.GainAmount(9, GR.Tooltips.Desecration, true), 7, 3, (c, p, m) -> PCLActions.Bottom.GainDesecration(9, false)),
        GainIntellect(ACTIONS.GainAmount(6, GR.Tooltips.Wisdom, true), 8, 2, (c, p, m) -> PCLActions.Bottom.GainWisdom(6, false)),
        GainStrength(ACTIONS.GainAmount(4, GR.Tooltips.Strength, true), 8, 4, (c, p, m) -> PCLActions.Bottom.GainStrength(4)),
        GainFocus(ACTIONS.GainAmount(3, GR.Tooltips.Focus, true), 8, 4, (c, p, m) -> PCLActions.Bottom.GainFocus(3)),
        ObtainGenshinCard(ACTIONS.ChooseMotivatedCard(CardSeries.GenshinImpact.Name, true), 9, 3, (c, p, m) -> {
            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pool = new RandomizedList<AbstractCard>(genshinCards.GetInnerList());

            while (choice.size() < 3 && pool.Size() > 0)
            {
                AbstractCard ca = pool.Retrieve(rng).makeCopy();
                ca.upgrade();
                choice.addToTop(ca);
            }
            PCLActions.Bottom.SelectFromPile(null, 1, choice)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            PCLActions.Bottom.MakeCardInHand(cards.get(0))
                                    .AddCallback(ca -> ca.modifyCostForCombat(-1));
                        }
                    });
        }),
        ObtainTokenDark(ACTIONS.AddToDrawPile(2, AffinityToken_Dark.DATA.Strings.NAME, true), 7, 3, (c, p, m) -> PCLActions.Bottom.MakeCardInDrawPile(AffinityToken.GetCopy(PCLAffinity.Dark, false))),
        ObtainWish(ACTIONS.AddToDrawPile(2, Traveler_Wish.DATA.Strings.NAME, true), 6, 4, (c, p, m) -> PCLActions.Bottom.MakeCardInDrawPile(new Traveler_Wish()));



        private final String text;
        private final int weight;
        private final int tier;
        private final ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action;

        AbyssPositiveEffect(String text, int weight, int tier, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> action) {
            this.text = text;
            this.weight = weight;
            this.tier = tier;
            this.action = action;
        }
    }
}
