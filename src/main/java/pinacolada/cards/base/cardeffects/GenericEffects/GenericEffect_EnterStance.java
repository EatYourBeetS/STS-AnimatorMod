package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.resources.GR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;

public class GenericEffect_EnterStance extends GenericEffect
{
    public static final String ID = Register(GenericEffect_EnterStance.class);

    protected final PCLStanceHelper stance;

    public GenericEffect_EnterStance(PCLStanceHelper stance)
    {
        super(ID, stance.ID, PCLCardTarget.Self, 1);
        this.stance = stance;
    }

    @Override
    public String GetText()
    {
        String text = stance.Tooltip.title.replace(stance.Affinity.PowerName, stance.Affinity.GetFormattedPowerSymbol());
        return GR.PCL.Strings.Actions.EnterStance("{" + text + "}", true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.ChangeStance(stance);
    }
}
