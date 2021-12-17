package pinacolada.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
        // TODO: Create a reusable method which replaces all keywords with their icons
        String text = tooltip.title
        .replace(GR.Tooltips.Velocity.title, "[R]")
        .replace(GR.Tooltips.Might.title, "[G]")
        .replace(GR.Tooltips.Wisdom.title, "[B]")
        .replace(GR.Tooltips.Endurance.title, "[O]")
        .replace(GR.Tooltips.Invocation.title, "[L]")
        .replace(GR.Tooltips.Desecration.title, "[D]");

        return GR.PCL.Strings.Actions.EnterStance("{" + text + "}", true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.ChangeStance(id);
    }
}
