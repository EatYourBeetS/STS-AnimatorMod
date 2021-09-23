package eatyourbeets.ui.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RotatingList;

import java.util.ArrayList;

public class DrawPileCardPreview
{
    public boolean enabled = true;
    public boolean requireTarget;

    protected static final float REFRESH_DELAY = 0.625f;
    protected final FuncT1<Boolean, AbstractCard> findCard;
    protected final ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards;
    protected final RotatingList<AbstractCard> cards;
    protected final float delay;
    protected boolean render;
    protected float lastTime;
    protected float timer;
    protected AbstractCard card = null;
    protected AbstractMonster lastTarget = null;

    public DrawPileCardPreview(FuncT1<Boolean, AbstractCard> findCard)
    {
        this(findCard, null);
    }

    public DrawPileCardPreview(ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        this(null, findCards);
    }

    protected DrawPileCardPreview(FuncT1<Boolean, AbstractCard> findCard, ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        this.cards = new RotatingList<>();
        this.findCard = findCard;
        this.findCards = findCards;
        this.timer = this.delay = 0.2f;
    }

    public DrawPileCardPreview SetEnabled(boolean enabled)
    {
        this.enabled = enabled;

        return this;
    }

    public DrawPileCardPreview RequireTarget(boolean requireTarget)
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
                    card.drawScale = 0.7f;
                    card.current_x = CardGroup.DRAW_PILE_X * 1.5f;
                    card.current_y = CardGroup.DRAW_PILE_Y * 3.5f;
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
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
        {
            int index = list.size();
            for (int i = 0; i < list.size(); i++)
            {
                final AbstractCard temp = list.get(i);
                if (temp.cardID.equals(c.cardID))
                {
                    index = -1;
                    break;
                }
                else if (c.name.compareTo(list.get(i).name) < 0)
                {
                    index = i;
                    break;
                }
            }

            if (index >= 0 && (findCard == null || findCard.Invoke(c)) && !c.hasTag(GR.Enums.CardTags.VOLATILE))
            {
                list.add(index, c);
            }
        }
    }
}
