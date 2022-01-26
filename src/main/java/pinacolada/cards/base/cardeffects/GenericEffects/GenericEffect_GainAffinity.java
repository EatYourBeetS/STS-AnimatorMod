package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

public class GenericEffect_GainAffinity extends GenericEffect
{
    public static final String ID = Register(GenericEffect_GainAffinity.class);

    protected final PCLAffinity affinity;

    public GenericEffect_GainAffinity(PCLAffinity affinity, int amount)
    {
        super(ID, affinity.Name, affinity.GetTooltip(), PCLCardTarget.Self, amount);
        this.affinity = affinity;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.GainAmount("+" + amount, affinity.GetTooltip(), true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLGameUtilities.AddAffinity(affinity,amount,true);
    }
}
