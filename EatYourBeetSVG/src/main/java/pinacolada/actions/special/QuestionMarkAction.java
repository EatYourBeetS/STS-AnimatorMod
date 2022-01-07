package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.pcl.colorless.QuestionMark;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLGameUtilities;

public class QuestionMarkAction extends EYBAction
{
    private final QuestionMark questionMark;

    public QuestionMarkAction(QuestionMark instance)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_XFAST);

        this.questionMark = instance;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        final AbstractCard copy = questionMark.copy = PCLGameUtilities.GetRandomCard();
        final int index = player.hand.group.indexOf(questionMark);
        if (copy != null && index >= 0)
        {
            if (copy instanceof PCLCard) {
                ((PCLCard) copy).affinities.Add(PCLAffinity.Star, 1);
                ((PCLCard) copy).triggerWhenCreated(false);
            }

            if (questionMark.upgraded)
            {
                copy.upgrade();
            }

            copy.current_x = questionMark.current_x;
            copy.current_y = questionMark.current_y;
            copy.target_x = questionMark.target_x;
            copy.target_y = questionMark.target_y;
            copy.uuid = questionMark.uuid;

            player.hand.group.remove(index);
            player.hand.group.add(index, copy);
            player.hand.glowCheck();

            PCLCombatStats.onStartOfTurn.Subscribe(questionMark);
        }

        Complete();
    }
}