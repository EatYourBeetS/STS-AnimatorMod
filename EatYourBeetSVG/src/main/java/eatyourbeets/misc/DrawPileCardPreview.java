package eatyourbeets.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.csharp.FuncT1;

public class DrawPileCardPreview
{
    private final FuncT1<AbstractCard, AbstractMonster> findCard;
    private float delay = 0.2f;
    private AbstractCard card = null;
    private AbstractMonster lastTarget = null;
    private AbstractMonster target = null;

    public DrawPileCardPreview(FuncT1<AbstractCard, AbstractMonster> findCard)
    {
        this.findCard = findCard;
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

            delay = 0.2f;
            lastTarget = target;
        }
        else
        {
            delay -= Gdx.graphics.getRawDeltaTime();
        }

        target = null;
    }

    public void Render(SpriteBatch sb)
    {
        if (card != null && delay < 0)
        {
            card.render(sb);
        }
    }

    public void SetCurrentTarget(AbstractMonster target)
    {
        this.target = target;
    }
}
