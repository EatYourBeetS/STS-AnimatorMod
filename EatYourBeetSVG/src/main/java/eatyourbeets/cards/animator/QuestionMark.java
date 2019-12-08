package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnStartOfTurnSubscriber;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class QuestionMark extends AnimatorCard implements OnStartOfTurnSubscriber
{
    public static final String ID = Register(QuestionMark.class.getSimpleName(), EYBCardBadge.Drawn);

    private static ArrayList<AnimatorCard> cardPool;
    private AnimatorCard copy = null;

    public QuestionMark()
    {
        super(ID, -2, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0, 0);

        SetSynergy(Synergies.ANY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new HigakiRinne()));
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
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

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper.AddToBottom(new QuestionMarkAction(this));
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

    private static AnimatorCard GetRandomCard()
    {
        if (cardPool == null)
        {
            cardPool = new ArrayList<>();
            for (AbstractCard c : CardLibrary.getAllCards())
            {
                if (c.type != CardType.CURSE && c.type != CardType.STATUS)
                {
                    if (c instanceof AnimatorCard &&
                            !(c instanceof QuestionMark) &&
                            !c.tags.contains(CardTags.HEALING) &&
                            c.rarity != CardRarity.BASIC)
                    {
                        cardPool.add((AnimatorCard) c);
                    }
                }
            }
        }

        return (AnimatorCard) JavaUtilities.GetRandomElement(cardPool).makeCopy();
    }

    private class QuestionMarkAction extends AbstractGameAction
    {
        private final QuestionMark instance;

        public QuestionMarkAction(QuestionMark instance)
        {
            this.instance = instance;
        }

        @Override
        public void update()
        {
            this.isDone = true;

            AbstractPlayer p = AbstractDungeon.player;
            int index = p.hand.group.indexOf(instance);

            instance.copy = GetRandomCard();

            AnimatorCard copy = instance.copy;
            if (copy != null && index >= 0)
            {
                //copy.originalName = instance.originalName;
                //copy.name = instance.name;
                //copy.rarity = instance.rarity;
                copy.SetSynergy(Synergies.ANY, true);

                if (instance.upgraded)
                {
                    copy.upgrade();
                }

                copy.current_x = instance.current_x;
                copy.current_y = instance.current_y;
                copy.target_x = instance.target_x;
                copy.target_y = instance.target_y;

                p.hand.group.remove(index);
                p.hand.group.add(index, copy);
                p.hand.glowCheck();

                PlayerStatistics.onStartOfTurn.Subscribe(instance);
            }
        }
    }
}