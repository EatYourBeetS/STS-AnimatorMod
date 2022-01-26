package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

public class GenericEffect_PayAffinity extends GenericEffect
{
    public static final String ID = Register(GenericEffect_PayAffinity.class);

    protected final PCLAffinity affinity;

    public GenericEffect_PayAffinity(PCLAffinity affinity, int amount)
    {
        super(ID, affinity.Name, affinity.GetTooltip(), PCLCardTarget.None, amount);
        this.affinity = affinity;
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
