package eatyourbeets.relics.animatorClassic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorClassicRelic;
import eatyourbeets.utilities.GameActions;

public class LivingPicture extends AnimatorClassicRelic implements OnSynergySubscriber
{
    public static final String ID = CreateFullID(LivingPicture.class);

    public LivingPicture()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();
        SetEnabled(true);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();
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