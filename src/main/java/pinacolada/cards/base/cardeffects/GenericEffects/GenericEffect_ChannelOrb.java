package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_ChannelOrb extends GenericEffect
{
    public static final String ID = Register(GenericEffect_ChannelOrb.class);

    protected final PCLOrbHelper orb;

    public GenericEffect_ChannelOrb(PCLOrbHelper orb, int amount)
    {
        super(ID, orb.ID, orb.Tooltip, PCLCardTarget.None, amount);
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.Channel(amount, tooltip, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.ChannelOrbs(orb, amount);
    }
}
