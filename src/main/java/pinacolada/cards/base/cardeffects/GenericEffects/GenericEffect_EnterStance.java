package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.stances.PCLStance;
import pinacolada.utilities.PCLActions;

public class GenericEffect_EnterStance extends GenericEffect
{
    public GenericEffect_EnterStance(String stance)
    {
        this.id = stance;
        this.tooltip = PCLStance.GetStanceTooltip(stance);
    }

    public GenericEffect_EnterStance(String stance, PCLCardTooltip tooltip)
    {
        this.id = stance;
        this.tooltip = tooltip;
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
        PCLActions.Bottom.ChangeStance(id);
    }
}
