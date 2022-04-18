package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.QuestionMarkAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class QuestionMark extends AnimatorCard implements OnStartOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(QuestionMark.class)
            .SetSkill(-2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetMaxCopies(0)
            .SetColor(CardColor.COLORLESS);

    public AnimatorCard copy = null;

    public QuestionMark()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1, 1, 0);
        SetVolatile(true);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Add(new QuestionMarkAction(this));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void OnStartOfTurn()
    {
        if (!player.hand.contains(copy))
        {
            if (TransformBack(player.drawPile) || TransformBack(player.discardPile) || TransformBack(player.exhaustPile))
            {
                CombatStats.onStartOfTurn.Unsubscribe(this);
            }
        }
    }

    private boolean TransformBack(CardGroup group)
    {
        int index = group.group.indexOf(copy);
        if (index >= 0)
        {
            group.group.remove(index);
            group.group.add(index, this);

            this.current_x = copy.current_x;
            this.current_y = copy.current_y;
            this.target_x  = copy.target_x;
            this.target_y  = copy.target_y;

            this.untip();
            this.stopGlowing();

            return true;
        }

        return false;
    }
}