package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLGameUtilities;

public class MantleOfTheStrategist extends PCLRelic implements OnSynergyCheckSubscriber
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

        PCLCombatStats.onSynergyCheck.Unsubscribe(this);
        PCLCombatStats.onSynergyCheck.Subscribe(this);
        SetEnabled(true);
    }



    @Override
    public boolean OnSynergyCheck(AbstractCard a, AbstractCard b)
    {
        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        AbstractCard lastCard = PCLGameUtilities.GetLastCardPlayed(true);
        return (lastCard == a) ? cardsPlayed == 2 : cardsPlayed == 1;
    }
}