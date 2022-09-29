package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class EngravedStaff extends AnimatorRelic implements OnAffinitySealedSubscriber
{
    public static final String ID = CreateFullID(EngravedStaff.class);

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        CombatStats.onAffinitySealed.ToggleSubscription(this, enabled);
    }

    @Override
    public void OnAffinitySealed(EYBCard card, boolean manual)
    {
        if (card.rarity == AbstractCard.CardRarity.RARE)
        {
            GameActions.Bottom.GainEnergy(1);
            flash();
        }
    }
}