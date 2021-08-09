package eatyourbeets.ui.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;

public class DrawPileCardPreview
{
    private final FuncT1<AbstractCard, AbstractMonster> findCard;
    private final float delay;
    private float timer;
    private AbstractCard card = null;
    private AbstractMonster lastTarget = null;
    private AbstractMonster target = null;

    public DrawPileCardPreview(FuncT1<AbstractCard, AbstractMonster> findCard)
    {
        this(0.2f, findCard);
    }

    public DrawPileCardPreview(float refreshDelay, FuncT1<AbstractCard, AbstractMonster> findCard)
    {
        this.findCard = findCard;
        this.timer = this.delay = refreshDelay;
    }

    public AbstractCard GetCurrentCard()
    {
        return card;
    }

    public void Update()
    {
        if (lastTarget != target)
        {
            if (target == null)
            {
                card = null;
            }
            else
            {
                FindCard();
            }

            timer = delay;
            lastTarget = target;
        }
        else
        {
            timer -= GR.UI.Delta();
        }

        target = null;
    }

    public void FindCard()
    {
        card = findCard.Invoke(target);

        if (card != null)
        {
            card.angle = 0;
            card.unfadeOut();
            card.lighten(true);
            card.drawScale = 0.7f;
            card.current_x = CardGroup.DRAW_PILE_X * 1.5f;
            card.current_y = CardGroup.DRAW_PILE_Y * 3.5f;
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (card != null && timer < 0)
        {
            card.render(sb);
        }
    }

    public void SetCurrentTarget(AbstractMonster target)
    {
        this.target = target;
    }
}
