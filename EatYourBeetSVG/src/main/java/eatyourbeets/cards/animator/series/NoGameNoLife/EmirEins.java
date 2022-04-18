package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class EmirEins extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EmirEins.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.General), false));

    public EmirEins()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Dark, 3);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.SelectFromPile(name, 1, GetReplacement(c, 2))
                .SetOptions(false, false)
                .AddCallback(cards2 ->
                {
                   for (AbstractCard c2 : cards2)
                   {
                       GameActions.Top.MakeCardInHand(c2);
                   }
                });
            }
        });

        if (info.TryActivateLimited() && CheckAffinity(Affinity.Dark) && CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.General, false);
        }
    }

    private CardGroup GetReplacement(AbstractCard card, int size)
    {
        final boolean isHindrance = GameUtilities.IsHindrance(card);
        final CardColor color = (isHindrance || card.color == CardColor.COLORLESS) ? player.getCardColor() : card.color;
        final CardRarity rarity;
        switch (card.rarity)
        {
            case BASIC:
            case COMMON:
            case UNCOMMON:
            case RARE:
                rarity = card.rarity;
                break;

            case SPECIAL:
                rarity = isHindrance ? CardRarity.COMMON : card.rarity;
                break;

            case CURSE:
            default:
                rarity = CardRarity.COMMON;
                break;
        }

        final boolean betaSeries = GR.Animator.Dungeon.HasBetaSeries;
        final RandomizedList<AbstractCard> randomCards = new RandomizedList<>();
        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.type == CardType.ATTACK && (rarity == CardRarity.SPECIAL || c.rarity == rarity) && !c.cardID.equals(card.cardID)
            && (c.color == CardColor.COLORLESS || c.color == color) && GameUtilities.IsObtainableInCombat(c))
            {
                if (!betaSeries && c instanceof AnimatorCard)
                {
                    final AnimatorCard c2 = (AnimatorCard) c;
                    final AnimatorLoadout loadout = GR.Animator.Data.GetLoadout(c2.series);
                    if (loadout == null || !loadout.IsBeta)
                    {
                        randomCards.Add(c);
                    }
                }
                else if (card instanceof EYBCard == c instanceof EYBCard)
                {
                    randomCards.Add(c);
                }
            }
        }

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < size && randomCards.Size() > 0)
        {
            final AbstractCard toAdd = randomCards.Retrieve(rng).makeCopy();
            //if (card.upgraded)
            //{
                toAdd.upgrade();
            //}
            group.group.add(toAdd);
        }

        return group;
    }
}