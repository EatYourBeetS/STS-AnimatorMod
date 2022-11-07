package eatyourbeets.actions.animatorClassic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animatorClassic.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class QuestionMarkAction extends EYBAction
{
    private static ArrayList<AnimatorClassicCard> cardPool;
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
        int index = player.hand.group.indexOf(questionMark);
        questionMark.copy = GetRandomCard();
        AnimatorClassicCard copy = questionMark.copy;

        if (copy != null && index >= 0)
        {
            copy.SetSeries(questionMark.copy.series);
            copy.SetShapeshifter();
            copy.triggerWhenCreated(false);

            if (questionMark.upgraded)
            {
                copy.upgrade();
            }

            copy.current_x = questionMark.current_x;
            copy.current_y = questionMark.current_y;
            copy.target_x = questionMark.target_x;
            copy.target_y = questionMark.target_y;

            player.hand.group.remove(index);
            player.hand.group.add(index, copy);
            player.hand.glowCheck();

            CombatStats.onStartOfTurn.Subscribe(questionMark);
        }

        Complete();
    }

    private static AnimatorClassicCard GetRandomCard()
    {
        if (cardPool == null)
        {
            cardPool = new ArrayList<>();

            for (AbstractCard c : GameUtilities.GetAvailableCards())
            {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS)
                {
                    if (c instanceof AnimatorClassicCard
                    && !(c instanceof QuestionMark)
                    && !c.tags.contains(AbstractCard.CardTags.HEALING)
                    && c.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        cardPool.add((AnimatorClassicCard)c);
                    }
                }
            }
        }

        return (AnimatorClassicCard) JUtils.Random(cardPool).makeCopy();
    }
}