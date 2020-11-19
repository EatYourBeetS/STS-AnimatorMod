package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class QuestionMarkAction extends EYBAction
{
    private static ArrayList<AnimatorCard> cardPool;
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
        AnimatorCard copy = questionMark.copy;

        if (copy != null && index >= 0)
        {
            copy.SetSynergy(Synergies.ANY);
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

    private static AnimatorCard GetRandomCard()
    {
        if (cardPool == null)
        {
            cardPool = new ArrayList<>();
            for (AbstractCard c : CardLibrary.getAllCards())
            {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS)
                {
                    if (c instanceof AnimatorCard
                    && !(c instanceof QuestionMark)
                    && !c.tags.contains(AbstractCard.CardTags.HEALING)
                    && c.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        cardPool.add((AnimatorCard)c);
                    }
                }
            }
        }

        return (AnimatorCard) JUtils.GetRandomElement(cardPool).makeCopy();
    }
}