package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_TriggerOrb extends GenericEffect
{
    public static final String ID = Register(GenericEffect_TriggerOrb.class);

    protected final PCLOrbHelper orb;

    public GenericEffect_TriggerOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, orb.ID, PCLCardTarget.None, amount);
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.Trigger(orb.Tooltip, amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.TriggerOrbPassive(amount).SetFilter(o -> o.ID.equals(orb.ID));
    }
}
