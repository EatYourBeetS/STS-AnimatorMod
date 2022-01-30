package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_ChannelOrb extends GenericEffect
{
    public static final String ID = Register(GenericEffect_ChannelOrb.class);

    protected final PCLOrbHelper orb;

    public GenericEffect_ChannelOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, orb != null ? orb.ID : null, PCLCardTarget.None, amount);
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return orb != null ? GR.PCL.Strings.Actions.Channel(amount, orb.Tooltip, true) : GR.PCL.Strings.Actions.ChannelRandomOrbs(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (orb != null) {
            PCLActions.Bottom.ChannelOrbs(orb, amount);
        }
        else {
            PCLActions.Bottom.ChannelRandomOrbs(amount);
        }
    }
}
