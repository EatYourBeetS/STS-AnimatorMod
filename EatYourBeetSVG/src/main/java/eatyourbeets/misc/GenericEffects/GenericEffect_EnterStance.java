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
        return GR.Animator.Strings.Actions.EnterStance("{" + tooltip.title + "}", true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(id);
    }
}
