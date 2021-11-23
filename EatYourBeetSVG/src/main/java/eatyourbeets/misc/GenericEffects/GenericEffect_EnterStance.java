package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.EYBStance;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_EnterStance extends GenericEffect
{
    public GenericEffect_EnterStance(String stance)
    {
        this.id = stance;
        this.tooltip = EYBStance.GetStanceTooltip(stance);
    }

    public GenericEffect_EnterStance(String stance, EYBCardTooltip tooltip)
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
        .replace(GR.Tooltips.Supercharge.title, "[L]")
        .replace(GR.Tooltips.Desecration.title, "[D]");

        return GR.Animator.Strings.Actions.EnterStance("{" + text + "}", true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(id);
    }
}
