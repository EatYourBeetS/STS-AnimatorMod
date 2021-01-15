package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;

public class MantleOfTheStrategist extends AnimatorRelic implements OnSynergyCheckSubscriber
{
    public static final String ID = CreateFullID(MantleOfTheStrategist.class);

    public MantleOfTheStrategist()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        CombatStats.onSynergyCheck.Unsubscribe(this);
        CombatStats.onSynergyCheck.SubscribeOnce(this);
        SetEnabled(true);
    }



    @Override
    public boolean OnSynergyCheck(AbstractCard a, AbstractCard b)
    {
        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        AbstractCard lastCard = GameUtilities.GetLastCardPlayed(true);
        return (lastCard == a) ? cardsPlayed == 2 : cardsPlayed == 1;
    }
}