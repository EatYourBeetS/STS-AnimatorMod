package eatyourbeets.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.misc.AinzEffects.*;
import eatyourbeets.misc.RandomizedList;

import java.util.ArrayList;

public class AinzAction extends AnimatorAction
{
    //private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("ExhaustAction").TEXT;
    private static final ArrayList<AinzEffect> effectPool = new ArrayList<>();

    private final RandomizedList<AinzEffect> effectList = new RandomizedList<>();
    private final ArrayList<AinzEffect> currentEffects = new ArrayList<>();
    private final boolean upgraded;

    public AinzAction(AbstractCreature target, boolean upgraded)
    {
        this.upgraded = upgraded;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;

        effectList.Clear();
        effectList.AddAll(effectPool);
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            currentEffects.clear();

            Random rng = AbstractDungeon.miscRng;
            for (int i = 0; i < 4 ; i++)
            {
                AinzEffect effect = effectList.Retrieve(rng);

                // Reduce the chances of Intangible
                if (effect instanceof AinzEffect_GainIntangible && rng.randomBoolean(0.25f))
                {
                    effect = effectList.Retrieve(rng);
                }

                currentEffects.add(effect);
            }

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AinzEffect e : currentEffects)
            {
                if (e != null)
                {
                    e.SetUpgraded(upgraded);
                    e.ainz.applyPowers();
                    e.ainz.initializeDescription();

                    group.addToTop(e.ainz);
                }
            }

            if (group.size() > 0)
            {
                AbstractDungeon.gridSelectScreen.open(group, 1, false, "");
            }
            else
            {
                logger.warn("No AinzEffect found in the effect pool");
                this.isDone = true; // Should never happen
            }
        }

        this.tickDuration();

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            card.untip();
            card.unhover();

            for (AinzEffect e : currentEffects)
            {
                if (e.ainz == card)
                {
                    e.EnqueueAction(AbstractDungeon.player);
                }
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    static
    {
        effectPool.add(new AinzEffect_ApplyBurning(19));
        effectPool.add(new AinzEffect_ApplyPoison(18));
        effectPool.add(new AinzEffect_ChannelRandomOrbs(23));
        effectPool.add(new AinzEffect_DamageAll(5));
        effectPool.add(new AinzEffect_DrawCards(14));
        effectPool.add(new AinzEffect_GainArtifactRemoveDebuffs(21));
        effectPool.add(new AinzEffect_GainDexterity(8));
        effectPool.add(new AinzEffect_GainEnergy(17));
        effectPool.add(new AinzEffect_GainFocus(16));
        effectPool.add(new AinzEffect_GainIntangible(20));
        effectPool.add(new AinzEffect_GainStrength(7));
        effectPool.add(new AinzEffect_GainTemporaryHP(15));
        effectPool.add(new AinzEffect_GainThorns(9));
        effectPool.add(new AinzEffect_PlayTopCard(22, 24));
    }
}
