package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.EYBStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GenericEffect_EnterStance extends GenericEffect
{
    public GenericEffect_EnterStance(String stanceID)
    {
        this.id = stanceID;
        this.tooltip = EYBStance.GetStanceTooltip(GameUtilities.GetPlayerClass(), stanceID);
    }

    public GenericEffect_EnterStance(String stanceID, EYBCardTooltip tooltip)
    {
        this.id = stanceID;
        this.tooltip = tooltip;
    }

    @Override
    public String GetText()
    {
        String text;

        // TODO: Create a reusable method which replaces all keywords with their icons
        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            text = tooltip.title
            .replace(GR.Tooltips.Agility.title, "[Agility]")
            .replace(GR.Tooltips.Force.title, "[Force]")
            .replace(GR.Tooltips.Intellect.title, "[Intellect]")
            .replace(GR.Tooltips.Corruption.title, "[Corruption]");
        }
        else
        {
            text = tooltip.title
            .replace(GR.Tooltips.Agility.title, "[A]")
            .replace(GR.Tooltips.Force.title, "[F]")
            .replace(GR.Tooltips.Intellect.title, "[I]")
            .replace(GR.Tooltips.Corruption.title, "[C]");
        }

        return GR.Animator.Strings.Actions.EnterStance("{" + text + "}", true);
    }

    @Override
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(id);
    }
}
