package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_NextTurnEnergy extends GenericEffect
{
    public GenericEffect_NextTurnEnergy(int amount)
    {
        this.amount = amount;
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
