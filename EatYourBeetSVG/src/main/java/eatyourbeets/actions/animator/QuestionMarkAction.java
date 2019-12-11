package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.animator.colorless.QuestionMark;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.JavaUtilities;

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
        AbstractPlayer p = AbstractDungeon.player;
        int index = p.hand.group.indexOf(questionMark);
        questionMark.copy = GetRandomCard();
        AnimatorCard copy = questionMark.copy;

        if (copy != null && index >= 0)
        {
            //copy.name = questionMark.name;
            //copy.rarity = questionMark.rarity;
            //copy.originalName = questionMark.originalName;
            copy.SetSynergy(Synergies.ANY, true);

            if (questionMark.upgraded)
            {
                copy.upgrade();
            }

            copy.current_x = questionMark.current_x;
            copy.current_y = questionMark.current_y;
            copy.target_x = questionMark.target_x;
            copy.target_y = questionMark.target_y;

            p.hand.group.remove(index);
            p.hand.group.add(index, copy);
            p.hand.glowCheck();

            PlayerStatistics.onStartOfTurn.Subscribe(questionMark);
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

        return (AnimatorCard) JavaUtilities.GetRandomElement(cardPool).makeCopy();
    }
}