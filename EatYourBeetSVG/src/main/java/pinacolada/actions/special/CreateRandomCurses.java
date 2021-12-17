package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.base.FakeAbstractCard;
import pinacolada.utilities.PCLActions;

public class CreateRandomCurses extends EYBActionWithCallback<AbstractCard>
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
            PCLActions.Top.MakeCard(GetRandomCurse(rng), destination)
            .AddCallback((ActionT1<AbstractCard>) this::Complete)
            .SetDuration(speed, true);
        }

        Complete();
    }

    public static AbstractCard GetRandomCurse(Random rng)
    {
        return CardLibrary.getCurse(fakeCurse, rng).makeCopy();
    }
}
