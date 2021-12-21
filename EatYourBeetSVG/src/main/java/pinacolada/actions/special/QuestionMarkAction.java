package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.pcl.colorless.QuestionMark;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class QuestionMarkAction extends EYBAction
{
    private static ArrayList<PCLCard> cardPool;
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
        final PCLCard copy = questionMark.copy = GetRandomCard();
        final int index = player.hand.group.indexOf(questionMark);
        if (copy != null && index >= 0)
        {
            copy.affinities.Set(PCLAffinity.Star, 2);
            copy.triggerWhenCreated(false);

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

    public static PCLCard GetRandomCard()
    {
        if (cardPool == null)
        {
            cardPool = new ArrayList<>();

            for (AbstractCard c : PCLGameUtilities.GetAvailableCards())
            {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS)
                {
                    if (c instanceof PCLCard
                    && !(c instanceof QuestionMark)
                    && !c.tags.contains(AbstractCard.CardTags.HEALING)
                    && c.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        cardPool.add((PCLCard)c);
                    }
                }
            }
        }

        return (PCLCard) PCLGameUtilities.GetRandomElement(cardPool).makeCopy();
    }
}