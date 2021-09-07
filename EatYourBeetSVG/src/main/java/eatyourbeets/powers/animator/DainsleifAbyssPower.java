package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.animator.beta.special.Traveler_Wish;
import eatyourbeets.cards.animator.beta.ultrarare.Dainsleif;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.cards.base.AnimatorCard_Dynamic;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DainsleifAbyssPower extends AnimatorPower {
    private final static HashMap<Integer, RandomizedList<AbyssNegativeEffect>> negativeEffectList = new HashMap<>();
    private final static RandomizedList<AbstractCard> genshinCards = new RandomizedList<>();
    private final static WeightedList<AbyssPositiveEffect> positiveEffectList = new WeightedList<>();
    private static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    private static final String NAME = Dainsleif.DATA.Strings.NAME;
    private static final int COUNTDOWN_AMT = 24;
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
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PURPLE, true));
            GameActions.Bottom.PlayFromPile(null, 1, null, player.drawPile, player.discardPile, player.hand).SetOptions(true, false).SetFilter(ca -> ca instanceof Dainsleif);
            ChooseEffect();
        }
        this.updateDescription();
    }

    private void InitializeLists() {
        if (genshinCards.Size() == 0) {
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c instanceof AnimatorCard && ((AnimatorCard) c).series != null && ((AnimatorCard) c).series.Equals(CardSeries.GenshinImpact) &&
                        (c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE)) {
                    genshinCards.Add(c);
                }
            }
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

        ArrayList<AnimatorCard_Dynamic> currentEffects = new ArrayList<>();

        while (currentEffects.size() < CHOICES) {
            AbyssPositiveEffect pEffect = tempPositiveEffects.Retrieve(rng);
            AbyssNegativeEffect nEffect = tempNegativeEffects.getOrDefault(pEffect.tier, new RandomizedList<>()).Retrieve(rng);
            if (nEffect != null) {
                AnimatorCardBuilder builder = Generate(pEffect, nEffect);
                currentEffects.add(builder.Build());
            }
            if (tempPositiveEffects.Size() == 0) {
                break;
            }
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AnimatorCard_Dynamic card : currentEffects) {
            if (card != null) {
                card.calculateCardDamage(null);
                group.addToTop(card);
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (!cards.isEmpty()) {
                        cards.get(0).use(player, null);
                    }
                });
    }

    public AnimatorCardBuilder Generate(AbyssPositiveEffect positiveEffect, AbyssNegativeEffect negativeEffect) {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(Dainsleif.DATA.ID + "Alt");
        String combinedText = positiveEffect.text + " NL " + negativeEffect.text;

        builder.SetText(NAME, combinedText, "");
        builder.SetProperties(AbstractCard.CardType.SKILL, GR.Animator.CardColor, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ALL);
        builder.SetOnUse((c, p, m) -> {
            positiveEffect.action.Invoke(c, p, m);
            negativeEffect.action.Invoke(c, p, m);
        });

        return builder;
    }


    private enum AbyssNegativeEffect {
        DiscardRandomCard(ACTIONS.DiscardRandom(1, true), 1, (c, p, m) -> GameActions.Bottom.DiscardFromHand(c.cardID, 1, true)),
        EnemiesGainBlock(ACTIONS.GiveAllEnemies(10, GR.Tooltips.Block, true), 1, (c, p, m) -> {
            for (AbstractMonster mo : GameUtilities.GetEnemies(true)) GameActions.Bottom.GainBlock(mo, 10);
        }),
        EnemiesGainStrength(ACTIONS.GiveAllEnemies(2, GR.Tooltips.Strength, true), 2, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Strength, 2)),
        EnemiesGainStrength2(ACTIONS.GiveAllEnemies(4, GR.Tooltips.Strength, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Strength, 4)),
        EnemiesGainTemporaryThorns(ACTIONS.GiveAllEnemies(5, GR.Tooltips.TemporaryThorns, true), 1, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.TemporaryThorns, 5)),
        PlayerGainBurning(ACTIONS.GainAmount(4, GR.Tooltips.Burning, true), 1, (c, p, m) -> GameActions.Bottom.ApplyBurning(null, p, 4)),
        PlayerGainFrail(ACTIONS.GainAmount(2, GR.Tooltips.Frail, true), 1, (c, p, m) -> GameActions.Bottom.ApplyFrail(null, p, 2)),
        PlayerGainFreezing(ACTIONS.GainAmount(4, GR.Tooltips.Freezing, true), 1, (c, p, m) -> GameActions.Bottom.ApplyFreezing(null, p, 4)),
        PlayerGainVulnerable(ACTIONS.GainAmount(2, GR.Tooltips.Vulnerable, true), 1, (c, p, m) -> GameActions.Bottom.ApplyVulnerable(null, p, 2)),
        PlayerGainVulnerable2(ACTIONS.GainAmount(3, GR.Tooltips.Vulnerable, true), 3, (c, p, m) -> GameActions.Bottom.ApplyVulnerable(null, p, 3)),
        PlayerGainWeak(ACTIONS.GainAmount(2, GR.Tooltips.Weak, true), 1, (c, p, m) -> GameActions.Bottom.ApplyWeak(null, p, 2)),
        PlayerGainWeak2(ACTIONS.GainAmount(3, GR.Tooltips.Weak, true), 3, (c, p, m) -> GameActions.Bottom.ApplyWeak(null, p, 3)),
        PlayerLoseBalance(ACTIONS.LosePower(1, GR.Tooltips.Balance, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Balance, -1)),
        PlayerLoseDexterity(ACTIONS.LosePower(1, GR.Tooltips.Dexterity, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Dexterity, -1)),
        PlayerLoseFocus(ACTIONS.LosePower(1, GR.Tooltips.Focus, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Focus, -1)),
        PlayerLoseStrength(ACTIONS.LosePower(1, GR.Tooltips.Strength, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Strength, -1)),
        PlayerTakeDamage(ACTIONS.TakeDamage(5, true), 2, (c, p, m) -> GameActions.Bottom.StackPower(new DelayedDamagePower(p, 5))),
        PlayerTakeDamage2(ACTIONS.TakeDamage(8, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(new DelayedDamagePower(p, 8))),
        RandomEnemyGainStrength(ACTIONS.GiveRandomEnemy(3, GR.Tooltips.Strength, true), 2, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Strength, 3)),
        RandomEnemyGainStrength2(ACTIONS.GiveRandomEnemy(6, GR.Tooltips.Strength, true), 3, (c, p, m) -> GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Strength, 6)),
        EndTurn(ACTIONS.DainsleifEndTurn(true), 4, (c, p, m) -> {
            GameActions.Last.Add(new PressEndTurnButtonAction());
        });


        private final String text;
        private final int tier;
        private final ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> action;

        AbyssNegativeEffect(String text, int tier, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> action) {
            this.text = text;
            this.tier = tier;
            this.action = action;
        }
    }

    private enum AbyssPositiveEffect {
        ApplyBlinded(ACTIONS.ApplyToALL(2, GR.Tooltips.Blinded, true), 10, 1, (c, p, m) -> GameActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), 2)),
        ApplyBurning(ACTIONS.ApplyToALL(5, GR.Tooltips.Burning, true), 10, 1, (c, p, m) -> GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), 4)),
        ApplyFreezing(ACTIONS.ApplyToALL(5, GR.Tooltips.Freezing, true), 10, 1, (c, p, m) -> GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), 4)),
        ApplyVulnerable(ACTIONS.ApplyToALL(3, GR.Tooltips.Vulnerable, true), 10, 1, (c, p, m) -> GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 3)),
        ApplyWeak(ACTIONS.ApplyToALL(3, GR.Tooltips.Weak, true), 10, 1, (c, p, m) -> GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 3)),
        ChannelRandomOrbs(ACTIONS.ChannelRandomOrbs(2, true), 10, 1, (c, p, m) -> GameActions.Bottom.ChannelRandomOrbs(2)),
        ChannelRandomOrbs2(ACTIONS.ChannelRandomOrbs(4, true), 10, 3, (c, p, m) -> GameActions.Bottom.ChannelRandomOrbs(4)),
        NextTurnDraw(ACTIONS.NextTurnDraw(3, true), 10, 1, (c, p, m) -> GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 3))),
        NextTurnEnergy(ACTIONS.NextTurnEnergy(2, true), 10, 1, (c, p, m) -> GameActions.Bottom.StackPower(new EnergizedPower(p, 2))),
        GainBlessing(ACTIONS.GainAmount(4, GR.Tooltips.Blessing, true), 8, 2, (c, p, m) -> GameActions.Bottom.GainBlessing(3, false)),
        GainCorruption(ACTIONS.GainAmount(6, GR.Tooltips.Corruption, true), 7, 3, (c, p, m) -> GameActions.Bottom.GainCorruption(4, false)),
        GainIntellect(ACTIONS.GainAmount(4, GR.Tooltips.Intellect, true), 8, 2, (c, p, m) -> GameActions.Bottom.GainIntellect(3, false)),
        ObtainGenshinCard(ACTIONS.ChooseMotivatedCard(CardSeries.GenshinImpact.Name, true), 7, 3, (c, p, m) -> {
            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pool = new RandomizedList<AbstractCard>(genshinCards.GetInnerList());

            while (choice.size() < 3 && pool.Size() > 0)
            {
                AbstractCard ca = pool.Retrieve(rng).makeCopy();
                ca.upgrade();
                choice.addToTop(ca);
            }
            GameActions.Bottom.SelectFromPile(null, 1, choice)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            GameActions.Bottom.MakeCardInHand(cards.get(0))
                                    .AddCallback(ca -> ca.modifyCostForCombat(-1));
                        }
                    });
        }),
        ObtainWish(ACTIONS.AddToDrawPile(2, Traveler_Wish.DATA.Strings.NAME, true), 6, 4, (c, p, m) -> GameActions.Bottom.MakeCardInDrawPile(new Traveler_Wish()));


        private final String text;
        private final int weight;
        private final int tier;
        private final ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> action;

        AbyssPositiveEffect(String text, int weight, int tier, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> action) {
            this.text = text;
            this.weight = weight;
            this.tier = tier;
            this.action = action;
        }
    }
}
