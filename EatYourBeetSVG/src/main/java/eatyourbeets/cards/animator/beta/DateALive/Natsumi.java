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
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

public class Natsumi extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Natsumi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    private static final int NUM_CHOICES = 7;
    private static Dictionary<Integer, ArrayList<AbstractCard>> cardPool;

    public Natsumi()
    {
        super(DATA);

        Initialize(0, 0);
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
                TransformCard(player, cards.get(0), NUM_CHOICES);
            }
        });
    }

    private void TransformCard(AbstractPlayer player, AbstractCard oldCard, int numOptions)
    {
        int cost = oldCard.cost;

        if (cardPool == null)
        {
            GenerateCardPool();
        }

        if (cardPool.get(cost) == null)
        {
            return;
        }

        RandomizedList<AbstractCard> randomizedOptions = new RandomizedList<AbstractCard>(cardPool.get(cost));
        CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        for (int i = 0; i < numOptions; i++)
        {
            if (randomizedOptions.Size() <= 0)
            {
                break;
            }

            options.addToTop(randomizedOptions.Retrieve(rng, true));
        }

        GameActions.Bottom.SelectFromPile(name, 1, options)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
        .SetOptions(false, false)
        .AddCallback(oldCard.uuid, (uuid, cards) ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.ReplaceCard((UUID) uuid, cards.get(0).makeCopy()).SetUpgrade(upgraded);
            }
        });
    }

    private static void GenerateCardPool()
    {
        cardPool = new Hashtable<>();

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS
            && c instanceof AnimatorCard && !c.tags.contains(AbstractCard.CardTags.HEALING)
            && c.rarity != AbstractCard.CardRarity.BASIC && c.rarity != AbstractCard.CardRarity.SPECIAL)
            {
                AddCardToPoolByCost(c);
            }
        }
    }

    private static void AddCardToPoolByCost(AbstractCard card)
    {
        ArrayList<AbstractCard> currentCost = cardPool.get(card.cost);
        if (currentCost == null)
        {
            currentCost = new ArrayList<>();
            cardPool.put(card.cost, currentCost);
        }

        currentCost.add(card);
    }
}