package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.FakeAbstractCard;
import eatyourbeets.utilities.GameActions;

public class CreateRandomCurses extends EYBAction
{
    protected static final FakeAbstractCard fakeCurse = new FakeAbstractCard(new AscendersBane()).SetID("-");
    protected final CardGroup destination;

    public CreateRandomCurses(int amount, CardGroup destination)
    {
        super(ActionType.CARD_MANIPULATION);

        this.destination = destination;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        final float speed = amount < 2 ? Settings.ACTION_DUR_FAST : amount < 3 ? Settings.ACTION_DUR_FASTER : Settings.ACTION_DUR_XFAST;
        for (int i = 0; i < amount; i++)
        {
            GameActions.Bottom.MakeCard(GetRandomCurse(AbstractDungeon.cardRandomRng), destination)
            .SetDuration(speed, true);
        }

        Complete();
    }

    public static AbstractCard GetRandomCurse(Random rng)
    {
        return CardLibrary.getCurse(fakeCurse, rng).makeCopy();
    }
}
