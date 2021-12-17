package pinacolada.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.delegates.FuncT0;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.utilities.PCLActions;

public class GenericEffect_TriggerOrb extends GenericEffect
{
    private final AbstractOrb orb;
    private final FuncT0<AbstractOrb> orbConstructor;

    public GenericEffect_TriggerOrb(AbstractOrb orb)
    {
        this.orbConstructor = null;
        this.orb = orb;
        this.amount = 1;
        this.tooltip = GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    public GenericEffect_TriggerOrb(int amount)
    {
        this.orbConstructor = null;
        this.orb = null;
        this.amount = amount;
        this.tooltip = GR.Tooltips.RandomOrb;
        this.id = tooltip.id;
    }

    public GenericEffect_TriggerOrb(FuncT0<AbstractOrb> orbConstructor, int amount)
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
        return GR.PCL.Strings.Actions.Trigger("[" + tooltip.id + "]", amount, true);
    }

    public PCLCardTooltip GetOrbTooltip(AbstractOrb orb)
    {
        return CardTooltips.FindByID(orb.ID.replace(PCLResources.ID + ":", ""));
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.TriggerOrbPassive(amount).SetFilter(o -> o.ID.equals(orb.ID));
    }
}
