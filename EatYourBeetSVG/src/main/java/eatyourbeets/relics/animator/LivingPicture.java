package eatyourbeets.relics.animator;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class LivingPicture extends AnimatorRelic implements OnSynergySubscriber
{
    public static final String ID = CreateFullID(LivingPicture.class);

    public LivingPicture()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        PlayerStatistics.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AnimatorCard card)
    {
        GameActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }
}