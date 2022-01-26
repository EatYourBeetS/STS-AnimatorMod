package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;

public class GenericEffect_EnterStance extends GenericEffect
{
    public static final String ID = Register(GenericEffect_EnterStance.class);

    protected final PCLStanceHelper stance;

    public GenericEffect_EnterStance(PCLStanceHelper stance)
    {
        super(ID, stance.ID, stance.Tooltip, PCLCardTarget.Self, 1);
        this.stance = stance;
    }

    @Override
    public String GetText()
    {
        String text = tooltip.title;
        for (PCLAffinity af : PCLAffinity.Basic()) {
            text = text.replace(af.PowerName, af.GetFormattedPowerSymbol());
        }

        return GR.PCL.Strings.Actions.EnterStance("{" + text + "}", true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.ChangeStance(stance);
    }
}
