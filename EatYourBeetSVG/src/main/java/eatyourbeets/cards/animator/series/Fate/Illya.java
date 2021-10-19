package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RotatingList;

public class Illya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Illya.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Illya()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0,0,-2);

        SetAffinity_Light();
        SetAffinity_Dark();
        SetAffinity_Mind();

        SetDrawPileCardPreview(this::FindCards);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final AbstractCard card = drawPileCardPreview.FindCard(m);
        if (card != null)
        {
            GameActions.Bottom.DealDamageAtEndOfTurn(p, p, magicNumber);
            GameActions.Bottom.PlayCard(card, p.drawPile, m);
        }
    }

    private void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        AbstractCard bestCard = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.ATTACK && GameUtilities.IsPlayable(c, target) && !c.tags.contains(GR.Enums.CardTags.VOLATILE)
                    && (GameUtilities.HasAffinity(c, Affinity.Light) || GameUtilities.HasAffinity(c, Affinity.Dark)))
            {
                c.calculateCardDamage(target);
                if (c.damage > maxDamage)
                {
                    maxDamage = c.damage;
                    bestCard = c;
                }
                c.resetAttributes();
            }
        }
        cards.Add(bestCard);
    }
}