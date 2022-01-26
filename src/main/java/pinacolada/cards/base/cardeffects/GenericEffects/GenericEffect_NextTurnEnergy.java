package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_NextTurnEnergy extends GenericEffect
{
    public static final String ID = Register(GenericEffect_NextTurnEnergy.class);

    public GenericEffect_NextTurnEnergy(int amount)
    {
        super(ID, null, GR.Tooltips.Energized, PCLCardTarget.None, amount);
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.NextTurnEnergy(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainEnergyNextTurn(amount);
    }
}
