package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.utilities.PCLActions;

public class CreateGriefSeeds extends EYBActionWithCallback<AbstractCard>
{
    protected boolean upgraded;
    protected CardGroup pile;

    public CreateGriefSeeds(int amount)
    {
        this(amount, null);
    }

    public CreateGriefSeeds(int amount, CardGroup pile)
    {
        super(ActionType.CARD_MANIPULATION);
        if (pile == null) {
            this.pile = player.hand;
        }
        else {
            this.pile = pile;
        }

        Initialize(amount);
    }

    public CreateGriefSeeds SetUpgrade(boolean upgraded)
    {
        this.upgraded = upgraded;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < amount; i++)
        {
            PCLActions.Top.MakeCard(new Curse_GriefSeed(), pile)
            .SetUpgrade(upgraded, false).CancelIfFull(true)
            .AddCallback((ActionT1<AbstractCard>) this::Complete)
            .SetDuration(Settings.ACTION_DUR_FASTER, false);
        }

        Complete();
    }
}
