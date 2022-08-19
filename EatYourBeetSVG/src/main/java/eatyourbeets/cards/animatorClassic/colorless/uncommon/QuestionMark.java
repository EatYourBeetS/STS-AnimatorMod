package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animatorClassic.QuestionMarkAction;
import eatyourbeets.cards.animatorClassic.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class QuestionMark extends AnimatorClassicCard implements OnStartOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(QuestionMark.class).SetSkill(-2, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public AnimatorClassicCard copy = null;

    public QuestionMark()
    {
        super(DATA);

        Initialize(0, 0);

        SetShapeshifter();
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
        GameActions.Bottom.MakeCardInHand(new HigakiRinne());
    }

    @Override
    public void OnStartOfTurn()
    {
        if (!player.hand.contains(copy))
        {
            if (transformBack(player.drawPile) || transformBack(player.discardPile) || transformBack(player.exhaustPile))
            {
                CombatStats.onStartOfTurn.Unsubscribe(this);
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