package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.misc.AinzEffects.*;
import eatyourbeets.powers.animator.AinzPower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

//Todo: Improve this
public class AinzAction extends EYBAction
{
    private final WeightedList<AinzEffect> effectList = new WeightedList<>();
    private final ArrayList<AinzEffect> currentEffects = new ArrayList<>();

    public AinzAction(int choices)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);

        Initialize(choices, AbstractResources.GetPowerStrings(AinzPower.POWER_ID).NAME);

        CreateEffectList();
    }

    @Override
    protected void FirstUpdate()
    {
        currentEffects.clear();

        Random rng = AbstractDungeon.cardRandomRng;
        for (int i = 0; i < amount ; i++)
        {
            currentEffects.add(effectList.Retrieve(rng));
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AinzEffect e : currentEffects)
        {
            if (e != null)
            {
                e.SetUpgraded(false);
                e.ainz.applyPowers();
                e.ainz.initializeDescription();

                group.addToTop(e.ainz);
            }
        }

        if (group.isEmpty())
        {
            JavaUtilities.GetLogger(this).warn("No AinzEffect found in the effect pool");
            Complete(); // Should never happen
        }
        else
        {
            AbstractDungeon.gridSelectScreen.open(group, 1, false, name);
        }
    }

    @Override
    protected void UpdateInternal()
    {
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

        tickDuration();
    }

    private void CreateEffectList()
    {
        effectList.Clear();
        effectList.Add(new AinzEffect_PlayTopCard(22, 24), 10);
        effectList.Add(new AinzEffect_ChannelRandomOrbs(23), 10);
        effectList.Add(new AinzEffect_GainTemporaryHP(15), 10);
        effectList.Add(new AinzEffect_ApplyBurning(19), 10);
        effectList.Add(new AinzEffect_ApplyPoison(18), 10);
        effectList.Add(new AinzEffect_DrawCards(14), 10);
        effectList.Add(new AinzEffect_GainThorns(9), 10);
        effectList.Add(new AinzEffect_DamageAll(5), 10);
        effectList.Add(new AinzEffect_GainIntellect(16), 8);
        effectList.Add(new AinzEffect_GainEnergy(17), 8);
        effectList.Add(new AinzEffect_GainAgility(8),8);
        effectList.Add(new AinzEffect_GainStrength(7), 8);
        effectList.Add(new AinzEffect_GainIntangibleLosePower(20),4);
        effectList.Add(new AinzEffect_GainArtifactRemoveDebuffs(21), 2);
    }
}
