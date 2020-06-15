package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Raven extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Raven.class).SetAttack(1, CardRarity.COMMON);

    private final DrawPileCardPreview drawPileCardPreview = new DrawPileCardPreview(Raven::FindBestCard);

    public Raven()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(3, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        drawPileCardPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        drawPileCardPreview.Update();
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library)
        {
            drawPileCardPreview.Render(sb);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
        }
        else
        {
            GameActions.Bottom.ApplyVulnerable(p, m, 1);
        }

        AbstractCard card = FindBestCard(m);
        if (card != null)
        {
            GameActions.Top.Draw(card);
        }
    }

    private static AbstractCard FindBestCard(AbstractMonster target)
    {
        AbstractCard selected = null;

        CardGroup drawPile = player.drawPile;
        if (drawPile.size() > 0)
        {
            int minDamage = Integer.MAX_VALUE;
            for (AbstractCard c : drawPile.getAttacks().group)
            {
                if (c.baseDamage < minDamage)
                {
                    minDamage = c.baseDamage;
                    selected = c;
                }
            }
        }

        return selected;
    }
}