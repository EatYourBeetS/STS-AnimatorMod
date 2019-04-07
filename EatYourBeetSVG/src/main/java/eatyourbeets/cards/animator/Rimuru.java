package eatyourbeets.cards.animator;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnEndOfTurnSubscriber;

import java.util.ArrayList;

public class Rimuru extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final String ID = CreateFullID(Rimuru.class.getSimpleName());

    private static ArrayList<AnimatorCard> cardPool;
    private AnimatorCard copy = null;

    public Rimuru()
    {
        super(ID, -2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0, 0);

        SetSynergy(Synergies.TenSura);
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
    public void OnEndOfTurn(boolean isPlayer)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (!p.hand.contains(copy))
        {
            if (!transformBack(p.drawPile))
            {
                if (!transformBack(p.discardPile))
                {
                    boolean reallyJava = transformBack(p.exhaustPile);
                }
            }

            PlayerStatistics.onEndOfTurn.Unsubscribe(this);
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper.AddToBottom(new RimuruAction(this));
    }

    private boolean transformBack(CardGroup group)
    {
        int index = group.group.indexOf(copy);
        if (index >= 0)
        {
            group.group.remove(index);
            group.group.add(index, this);
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
                            !(c instanceof Rimuru) &&
                            !c.tags.contains(CardTags.HEALING) &&
                            !c.tags.contains(BaseModCardTags.BASIC_DEFEND) &&
                            !c.tags.contains(BaseModCardTags.BASIC_STRIKE))
                    {
                        cardPool.add((AnimatorCard) c);
                    }
                }
            }
        }

        return (AnimatorCard) Utilities.GetRandomElement(cardPool).makeCopy();
    }

    private class RimuruAction extends AnimatorAction
    {
        private final Rimuru rimuru;

        public RimuruAction(Rimuru rimuru)
        {
            this.rimuru = rimuru;
        }

        @Override
        public void update()
        {
            this.isDone = true;

            AbstractPlayer p = AbstractDungeon.player;
            int index = p.hand.group.indexOf(rimuru);

            rimuru.copy = GetRandomCard();

            AnimatorCard copy = rimuru.copy;
            if (copy != null && index >= 0)
            {
                copy.originalName = rimuru.originalName;
                copy.name = rimuru.name;
                copy.rarity = rimuru.rarity;
                copy.anySynergy = true;

                if (rimuru.upgraded)
                {
                    copy.upgrade();
                }

                copy.current_x = rimuru.current_x;
                copy.current_y = rimuru.current_y;
                copy.target_x = rimuru.target_x;
                copy.target_y = rimuru.target_y;

                p.hand.group.remove(index);
                p.hand.group.add(index, copy);
                p.hand.glowCheck();

                PlayerStatistics.onEndOfTurn.Subscribe(rimuru);
            }
        }
    }
}