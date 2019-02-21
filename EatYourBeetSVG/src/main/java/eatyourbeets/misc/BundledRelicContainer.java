package eatyourbeets.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class BundledRelicContainer
{
    public final RewardItem rewardItem;
    public final ArrayList<BundledRelic> bundledRelics;

    public BundledRelicContainer(RewardItem rewardItem)
    {
        this.rewardItem = rewardItem;
        this.bundledRelics = new ArrayList<>();
    }

    public void Open(ArrayList<AbstractCard> cards)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            for (AbstractCard card : cards)
            {
                if (bundledRelic.cardID.equals(card.cardID))
                {
                    bundledRelic.card = card;
                    bundledRelic.Open();
                }
            }
        }
    }

    public void Update(CardRewardScreen screen)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            bundledRelic.Update(screen);
        }
    }

    public void Render(CardRewardScreen screen, SpriteBatch sb)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            bundledRelic.Render(screen, sb);
        }
    }

    public void Acquired(AbstractCard hoveredCard)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            if (bundledRelic.card == hoveredCard)
            {
                bundledRelic.Acquired();
            }
        }
    }

    public void Remove(AbstractCard card)
    {
        for (int i = 0; i < bundledRelics.size(); i++)
        {
            if (bundledRelics.get(i).card == card)
            {
                bundledRelics.remove(i);
                return;
            }
        }
    }
}
