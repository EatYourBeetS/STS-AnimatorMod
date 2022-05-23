package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

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
        final AnimatorCard copy = questionMark.copy = GetRandomCard();
        final int index = player.hand.group.indexOf(questionMark);
        if (copy != null && index >= 0)
        {
            for (EYBCardAffinity a : copy.affinities.List)
            {
                a.level = 0;
            }

            copy.affinities.Set(Affinity.Star, questionMark.upgraded ? 2 : 1);
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

            //CombatStats.onStartOfTurn.Subscribe(questionMark);
        }

        Complete();
    }

    private static AnimatorCard GetRandomCard()
    {
        if (cardPool == null)
        {
            cardPool = new ArrayList<>();

            for (AbstractCard c : GameUtilities.GetAvailableCards())
            {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS)
                {
                    if (c instanceof AnimatorCard
                    && !(c instanceof QuestionMark)
                    && GameUtilities.IsObtainableInCombat(c)
                    && c.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        cardPool.add((AnimatorCard)c);
                    }
                }
            }
        }

        return (AnimatorCard) GameUtilities.GetRandomElement(cardPool).makeCopy();
    }
}