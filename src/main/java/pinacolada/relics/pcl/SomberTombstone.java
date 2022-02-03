package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class SomberTombstone extends PCLRelic  implements OnCardCreatedSubscriber
{
    public static final String ID = CreateFullID(SomberTombstone.class);

    public SomberTombstone()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLCombatStats.onCardCreated.Subscribe(this);

        PCLActions.Top.MoveCards(player.hand, player.exhaustPile).SetFilter(c -> c.hasTag(AFTERLIFE)).ShowEffect(false, false, 0.0f);
        PCLActions.Top.MoveCards(player.drawPile, player.exhaustPile).SetFilter(c -> c.hasTag(AFTERLIFE)).ShowEffect(false, false, 0.0f);
        PCLActions.Top.MoveCards(player.discardPile, player.exhaustPile).SetFilter(c -> c.hasTag(AFTERLIFE)).ShowEffect(false, false, 0.0f);

        flash();
    }

    @Override
    public void OnCardCreated(AbstractCard card, boolean startOfBattle)
    {
        if (card.hasTag(AFTERLIFE))
        {
            PCLActions.Top.MoveCard(card, player.exhaustPile);
            flash();
        }
    }

}