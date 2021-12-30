package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.interfaces.delegates.FuncT0;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.utilities.PCLActions;

public class GenericEffect_ChannelOrb extends GenericEffect
{
    private AbstractOrb orb;
    private FuncT0<AbstractOrb> orbConstructor;

    public GenericEffect_ChannelOrb(AbstractOrb orb)
    {
        this.orbConstructor = null;
        this.orb = orb;
        this.amount = 1;
        this.tooltip = GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    public GenericEffect_ChannelOrb(int amount)
    {
        this.orbConstructor = null;
        this.orb = null;
        this.amount = amount;
        this.tooltip = GR.Tooltips.RandomOrb;
        this.id = tooltip.id;
    }

    public GenericEffect_ChannelOrb(FuncT0<AbstractOrb> orbConstructor, int amount)
    {
        this.orbConstructor = orbConstructor;
        this.orb = orbConstructor.Invoke();
        this.amount = amount;
        this.tooltip =  GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.Channel(amount, "[" + tooltip.id + "]", true);
    }

    public PCLCardTooltip GetOrbTooltip(AbstractOrb orb)
    {
        // Lightning and Dark use tooltips with ~
        switch (orb.ID) {
            case Lightning.ORB_ID:
                return GR.Tooltips.Lightning;
            case Dark.ORB_ID:
                return GR.Tooltips.Dark;
            default:
                return CardTooltips.FindByID(orb.ID.replace(PCLResources.ID + ":", ""));
        }
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (GR.Tooltips.RandomOrb.Is(tooltip))
        {
            PCLActions.Bottom.ChannelRandomOrbs(amount);
        }
        else if (amount > 1)
        {
            PCLActions.Bottom.ChannelOrbs(orbConstructor, amount);
        }
        else
        {
            PCLActions.Bottom.ChannelOrb(orb);
        }
    }
}
