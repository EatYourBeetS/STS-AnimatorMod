package eatyourbeets.ui.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.ListSelection;
import eatyourbeets.utilities.RotatingList;

import java.util.ArrayList;

public class CardPreview
{
    public boolean enabled = true;
    public boolean requireTarget;

    protected static final float REFRESH_DELAY = 0.625f;
    protected final FuncT1<Boolean, AbstractCard> findCard;
    protected final ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards;
    protected final Vector2 cardPosition = new Vector2(CardGroup.DRAW_PILE_X * 1.5f, CardGroup.DRAW_PILE_Y * 3.5f);
    protected final RotatingList<AbstractCard> cards;
    protected final float delay;
    protected boolean render;
    protected float lastTime;
    protected float timer;
    protected int size;
    protected ListSelection<AbstractCard> selection = CardSelection.Bottom;
    protected CardGroup.CardGroupType groupType;
    protected AbstractCard card = null;
    protected AbstractMonster lastTarget = null;

    public CardPreview(FuncT1<Boolean, AbstractCard> findCard)
    {
        this(findCard, null);
    }

    public CardPreview(ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        this(null, findCards);
    }

    protected CardPreview(FuncT1<Boolean, AbstractCard> findCard, ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        this.cards = new RotatingList<>();
        this.groupType = CardGroup.CardGroupType.DRAW_PILE;
        this.findCard = findCard;
        this.findCards = findCards;
        this.timer = this.delay = 0.2f;
    }

    public CardPreview SetSelection(ListSelection<AbstractCard> selection, int size)
    {
        this.size = size;
        this.selection = selection;

        if (selection.mode.IsRandom())
        {
            throw new RuntimeException("Random selection not supported");
        }

        return this;
    }

    public CardPreview SetGroupType(CardGroup.CardGroupType type)
    {
        this.groupType = type;

        if (type == CardGroup.CardGroupType.DRAW_PILE || type == CardGroup.CardGroupType.HAND || type == CardGroup.CardGroupType.UNSPECIFIED)
        {
            cardPosition.x = Settings.WIDTH * 0.06f;
            cardPosition.y = Settings.scale * 175f;
        }
        else
        {
            cardPosition.x = Settings.WIDTH * 0.94f;
            cardPosition.y = Settings.HEIGHT * 0.4f;
        }

        return this;
    }

    public CardPreview SetEnabled(boolean enabled)
    {
        this.enabled = enabled;

        return this;
    }

    public CardPreview RequireTarget(boolean requireTarget)
    {
        this.requireTarget = requireTarget;

        return this;
    }

    public AbstractCard GetCurrentCard()
    {
        return card;
    }

    public void Update(AbstractCard source, AbstractMonster target)
    {
        final float currentTime = GR.UI.Time();
        if (lastTarget != target || (currentTime - lastTime) > REFRESH_DELAY)
        {
            if (target == null && (requireTarget || AbstractDungeon.player.hoveredCard != source || !AbstractDungeon.player.isDraggingCard))
            {
                card = null;
            }
            else
            {
                final AbstractCard c = FindCard(target);
                if (c != card && c != null)
                {
                    if (card != null)
                    {
                        timer = 0;
                    }
                    else
                    {
                        timer = delay;
                    }

                    card = c;
                    card.angle = 0;
                    card.unfadeOut();
                    card.lighten(true);
                    card.drawScale = card.targetDrawScale = 0.7f;
                    card.current_x = card.target_x = cardPosition.x;
                    card.current_y = card.target_y = cardPosition.y;
                }
            }

            lastTime = currentTime;
            lastTarget = target;
        }
        else if (timer > 0)
        {
            timer -= GR.UI.Delta();
        }

        render = timer <= 0;
    }

    public AbstractCard FindCard(AbstractMonster target)
    {
        final int previousSize = cards.Count();
        final int previousIndex = cards.GetIndex();

        FindCards(cards, target);

        cards.SetIndex((cards.Count() == previousSize) ? previousIndex : 0);

        return (cards.Count() > 0) ? cards.Next(true) : null;
    }

    public void Render(SpriteBatch sb)
    {
        if (render && card != null)
        {
            if ((GR.UI.Time() - lastTime) > REFRESH_DELAY)
            {
                render = false;
            }
            else
            {
                card.render(sb);
            }
        }
    }

    protected void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        if (findCards != null)
        {
            findCards.Invoke(cards, target);
            return;
        }

        cards.Clear();

        final ArrayList<AbstractCard> list = cards.GetInnerList();
        final ArrayList<AbstractCard> pile;
        switch (groupType)
        {
            case DRAW_PILE:
                pile = AbstractDungeon.player.drawPile.group;
                break;
            case MASTER_DECK:
                pile = AbstractDungeon.player.masterDeck.group;
                break;
            case HAND:
                pile = AbstractDungeon.player.hand.group;
                break;
            case DISCARD_PILE:
                pile = AbstractDungeon.player.discardPile.group;
                break;
            case EXHAUST_PILE:
                pile = AbstractDungeon.player.exhaustPile.group;
                break;
            case CARD_POOL:
                pile = CardLibrary.getAllCards();
                break;

            case UNSPECIFIED:
            default:
                return;
        }

        for (int i = 0; i < pile.size(); i++)
        {
            final AbstractCard c = selection.Get(pile, i, false);

            int index = list.size();
            for (int j = 0; j < list.size(); j++)
            {
                final AbstractCard temp = list.get(j);
                if (temp.cardID.equals(c.cardID))
                {
                    index = -1;
                    break;
                }
                else if (c.name.compareTo(list.get(j).name) < 0)
                {
                    index = j;
                    break;
                }
            }

            if (index >= 0 && (findCard == null || findCard.Invoke(c)) && !c.hasTag(EYBCard.VOLATILE))
            {
                list.add(index, c);

                if (size > 0 && list.size() >= size)
                {
                    return;
                }
            }
        }
    }
}
