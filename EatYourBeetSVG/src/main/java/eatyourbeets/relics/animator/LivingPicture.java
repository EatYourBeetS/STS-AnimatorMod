package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
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

        CombatStats.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AbstractCard c)
    {
        GameActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }
}