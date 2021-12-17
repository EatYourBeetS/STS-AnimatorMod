package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.actions.special.HigakiRinneAction;
import pinacolada.cards.pcl.series.Katanagatari.HigakiRinne;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class HigakiRinnePower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(HigakiRinnePower.class);

    private final HigakiRinne higakiRinne;

    public HigakiRinnePower(AbstractCreature owner, HigakiRinne higakiRinne, int amount)
    {
        super(owner, POWER_ID);

        this.higakiRinne = higakiRinne;

        Initialize(amount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        PCLActions.Bottom.Add(new HigakiRinneAction(higakiRinne, amount));
        this.flash();
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new HigakiRinnePower(owner, higakiRinne, amount);
    }
}
