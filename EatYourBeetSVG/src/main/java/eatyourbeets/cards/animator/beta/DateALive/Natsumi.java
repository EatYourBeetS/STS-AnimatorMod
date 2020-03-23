package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Natsumi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Natsumi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    private static Dictionary<Integer, ArrayList<AbstractCard>> cardPool;

    public Natsumi()
    {
        super(DATA);

        Initialize(0, 0, 7);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                TransformCard(cards.get(0), magicNumber);
            }
        });
    }

    private void TransformCard(AbstractCard toReplace, int numOptions)
    {
        if (cardPool == null)
        {
            GenerateCardPool();
        }

        ArrayList<AbstractCard> cardsWithSameCost = cardPool.get(toReplace.costForTurn);
        if (cardsWithSameCost == null || cardsWithSameCost.isEmpty())
        {
            return;
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        RandomizedList<AbstractCard> options = new RandomizedList<>(cardsWithSameCost);

        for (int i = 0; i < numOptions && options.Size() > 0; i++)
        {
            group.addToTop(options.Retrieve(rng, true).makeCopy());
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
        .SetOptions(false, false)
        .AddCallback(toReplace, (card, cards) ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.ReplaceCard(((AbstractCard)card).uuid, cards.get(0))
                .SetUpgrade(((AbstractCard)card).upgraded);
            }
        });
    }

    private static void GenerateCardPool()
    {
        cardPool = new Hashtable<>();

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c instanceof AnimatorCard && !GameUtilities.IsCurseOrStatus(c)
            && !c.hasTag(AbstractCard.CardTags.HEALING)
            && c.rarity != AbstractCard.CardRarity.SPECIAL
            && c.rarity != AbstractCard.CardRarity.BASIC)
            {
                ArrayList<AbstractCard> currentCost = cardPool.get(c.cost);
                if (currentCost == null)
                {
                    currentCost = new ArrayList<>();
                    cardPool.put(c.cost, currentCost);
                }

                currentCost.add(c);
            }
        }
    }
}