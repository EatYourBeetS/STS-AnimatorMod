package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RotatingList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Illya extends PCLCard
{
    public static final PCLCardData DATA = Register(Illya.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Berserker(), false));

    public Illya()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetCostUpgrade(-1);

        SetAffinity_Star(1);

        SetDrawPileCardPreview(this::FindCards);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.Callback(() ->
        {
            if (!DrawBerserker(player.drawPile))
            {
                if (!DrawBerserker(player.discardPile))
                {
                    if (!DrawBerserker(player.exhaustPile))
                    {
                        DrawBerserker(player.hand);
                    }
                }
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final AbstractCard card = drawPileCardPreview.FindCard(m);
        if (card != null)
        {
            PCLActions.Bottom.DealDamageAtEndOfTurn(p, p, magicNumber);
            PCLActions.Bottom.PlayCard(card, p.drawPile, m);
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
                    PCLGameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    PCLActions.Top.MoveCard(c, group, player.hand)
                    .ShowEffect(true, true)
                    .AddCallback(PCLGameUtilities::Retain);
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
            if (c.type == CardType.ATTACK && PCLGameUtilities.IsPlayable(c, target) && !c.tags.contains(GR.Enums.CardTags.VOLATILE))
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