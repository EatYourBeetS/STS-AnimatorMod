package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RotatingList;

public class Illya extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Illya.class).SetSeriesFromClassPackage()
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Berserker(), false));

    public Illya()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -2);

        SetCardPreview(this::FindCards);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(() ->
        {
            boolean __ = DrawBerserker(player.drawPile)  || DrawBerserker(player.discardPile)
                    || DrawBerserker(player.exhaustPile) || DrawBerserker(player.hand);
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final AbstractCard card = cardPreview.FindCard(m);
        if (card != null)
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(magicNumber);
            GameActions.Bottom.PlayCard(card, p.drawPile, m);
        }
    }

    private boolean DrawBerserker(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Berserker.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, group, player.hand)
                            .ShowEffect(true, true)
                            .AddCallback(GameUtilities::Retain);
                }

                return true;
            }
        }

        return false;
    }

    private void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        AbstractCard bestCard = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.ATTACK && GameUtilities.IsPlayable(c, target) && !c.tags.contains(VOLATILE))
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