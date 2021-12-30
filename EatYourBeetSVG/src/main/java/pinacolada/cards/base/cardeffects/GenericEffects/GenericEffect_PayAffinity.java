package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

public class GenericEffect_PayAffinity extends GenericEffect
{
    protected final PCLAffinity affinity;

    public GenericEffect_PayAffinity(PCLAffinity affinity, int amount)
    {
        this.affinity = affinity;
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.PayCost(amount, affinity.GetTooltip(), true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLGameUtilities.TrySpendAffinity(affinity,amount,true);
    }
}
