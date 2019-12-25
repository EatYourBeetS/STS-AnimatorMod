package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.QuestionMarkAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.OnStartOfTurnSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;

public class QuestionMark extends AnimatorCard implements OnStartOfTurnSubscriber
{
    public static final String ID = Register(QuestionMark.class.getSimpleName(), EYBCardBadge.Drawn);

    public AnimatorCard copy = null;

    public QuestionMark()
    {
        super(ID, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0, 0);

        SetSynergy(Synergies.ANY);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Add(new QuestionMarkAction(this));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.MakeCardInHand(new HigakiRinne());
    }

    @Override
    public void OnStartOfTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        if (!p.hand.contains(copy))
        {
            if (transformBack(p.drawPile) || transformBack(p.discardPile) || transformBack(p.exhaustPile))
            {
                PlayerStatistics.onStartOfTurn.Unsubscribe(this);
            }
        }
    }

    private boolean transformBack(CardGroup group)
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