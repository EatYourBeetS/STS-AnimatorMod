package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class WitchsHeartFlames extends AnimatorRelic implements OnCardCreatedSubscriber
{
    public static final String ID = CreateFullID(WitchsHeartFlames.class);

    public WitchsHeartFlames()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        CombatStats.onCardCreated.Subscribe(this);
    }

    @Override
    public void OnCardCreated(AbstractCard card, boolean startOfBattle)
    {
        if (card instanceof Burn)
        {
            GameActions.Last.ReplaceCard(card.uuid, new SearingBurn());
            flash();
        }
    }
}