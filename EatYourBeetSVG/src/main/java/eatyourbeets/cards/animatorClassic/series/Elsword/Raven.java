package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RotatingList;

public class Raven extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Raven.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON);

    public Raven()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(3, 0);
        SetScaling(0, 1, 0);

        SetCardPreview(this::FindCards).RequireTarget(true).SetGroupType(CardGroup.CardGroupType.DRAW_PILE);

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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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

        AbstractCard card = cardPreview.FindCard(m);
        if (card != null)
        {
            GameActions.Bottom.Draw(card);
        }
    }

    private void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        AbstractCard bestCard = null;
        int minDamage = Integer.MAX_VALUE;
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.ATTACK && GameUtilities.IsPlayable(c, target) && !c.tags.contains(VOLATILE))
            {
                c.calculateCardDamage(target);
                if (c.damage < minDamage)
                {
                    minDamage = c.damage;
                    bestCard = c;
                }
                c.resetAttributes();
            }
        }
        cards.Add(bestCard);
    }
}