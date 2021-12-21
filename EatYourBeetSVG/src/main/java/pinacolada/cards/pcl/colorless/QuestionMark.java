package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import pinacolada.actions.special.QuestionMarkAction;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class QuestionMark extends PCLCard implements OnStartOfTurnSubscriber
{
    public static final PCLCardData DATA = Register(QuestionMark.class)
            .SetSkill(-2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS);

    public PCLCard copy = null;

    public QuestionMark()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1, 0, 0);
        SetVolatile(true);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.Add(new QuestionMarkAction(this));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void OnStartOfTurn()
    {
        super.OnStartOfTurn();
        if (!player.hand.contains(copy))
        {
            if (TransformBack(player.drawPile) || TransformBack(player.discardPile) || TransformBack(player.exhaustPile))
            {
                PCLCombatStats.onStartOfTurn.Unsubscribe(this);
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