package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Curse_Rozen extends PCLCard_Curse implements OnSynergySubscriber, Hidden
{
    public static final PCLCardData DATA = Register(Curse_Rozen.class)
    		.SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, false).SetSeries(CardSeries.RozenMaiden);

    public Curse_Rozen()
    {
        super(DATA, true);
        SetAffinity_Dark(1);

        Initialize(0, 0);
        SetUnplayable(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            PCLCombatStats.onSynergy.Subscribe(this);
        }
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        if (player.hand.contains(this))
        {
            flash();
            PCLActions.Top.MoveCard(this, player.hand, player.drawPile)
                    .SetDestination(CardSelection.Top);
        }
        PCLCombatStats.onSynergy.Unsubscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}

// Unplayable.
// While in hand, put this on top of your draw pile whenever you Synergize.