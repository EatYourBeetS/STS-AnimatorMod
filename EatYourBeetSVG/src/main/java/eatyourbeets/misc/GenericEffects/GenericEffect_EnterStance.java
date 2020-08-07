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
    protected String stanceID;
    protected EYBCardTooltip tooltip;

    public GenericEffect_EnterStance(String stance)
    {
        this.stanceID = stance;
        this.tooltip = EYBStance.GetStanceTooltip(stance);
    }

    public GenericEffect_EnterStance(String stance, EYBCardTooltip tooltip)
    {
        this.stanceID = stance;
        this.tooltip = tooltip;
    }

    @Override
    public String GetText()
    {
        // TODO: Create a reusable method which replaces all keywords with their icons
        String text = tooltip.title
        .replace(GR.Tooltips.Agility.title, "[A]")
        .replace(GR.Tooltips.Force.title, "[F]")
        .replace(GR.Tooltips.Intellect.title, "[I]");

        return GR.Animator.Strings.Actions.EnterStance("{" + text + "}", true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(stanceID);
    }
}
