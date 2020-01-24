package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.ui.GUIElement;

import java.util.ArrayList;

public class BundledRelicContainer extends GUIElement
{
    public final ArrayList<BundledRelic> bundledRelics;
    public RewardItem rewardItem;

    public BundledRelicContainer()
    {
        this(null);
    }

    public BundledRelicContainer(RewardItem rewardItem)
    {
        this.rewardItem = rewardItem;
        this.bundledRelics = new ArrayList<>();
    }

    public void Open(RewardItem rewardItem, ArrayList<AbstractCard> cards)
    {
        BundledRelicProvider.SetupBundledRelics(this, rewardItem, cards);

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

    public void Close()
    {
        rewardItem = null;
        bundledRelics.clear();
    }

    public void Update()
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            bundledRelic.Update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            bundledRelic.Render(sb);
        }
    }

    public void OnCardObtained(AbstractCard hoveredCard)
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
