package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.Katanagatari.Hitei;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class HiteiAction extends EYBAction
{
    public HiteiAction(int cards)
    {
        super(ActionType.EXHAUST, Settings.ACTION_DUR_FAST);

        Initialize(cards, Hitei.DATA.Strings.NAME);
    }

    @Override
    protected void FirstUpdate()
    {
        RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
        randomizedList.AddAll(player.drawPile.group);
        randomizedList.AddAll(player.discardPile.group);

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (int i = 0; i < amount; i++)
        {
            if (randomizedList.Size() > 0)
            {
                group.addToTop(randomizedList.Retrieve(AbstractDungeon.cardRandomRng));
            }
        }

        if (group.size() > 0)
        {
            GameActions.Top.ExhaustFromPile(name, 1, group);
        }
        else
        {
            Complete();
        }
    }
}
