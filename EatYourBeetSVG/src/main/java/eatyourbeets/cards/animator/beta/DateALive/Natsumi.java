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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Natsumi extends AnimatorCard {
    public static final EYBCardData DATA = Register(Natsumi.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    private static Dictionary<Integer, CardGroup> cardPool;
    private static AbstractCard newCard;

    public Natsumi() {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.DateALive);
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
                TransformCard(p, cards.get(0), GameUtilities.GetCurrentDeckSize(p) < 30);
            }
        });
    }

    private void TransformCard(AbstractPlayer player, AbstractCard oldCard, boolean isRandom)
    {
        GameActions.Bottom.Purge(oldCard);

        int index = player.hand.group.indexOf(oldCard);

        if (index < 0)
        {
            return;
        }

        SetNewCardToTransform(oldCard, isRandom, name, cardData.Strings.EXTENDED_DESCRIPTION[1]);

        if (newCard != null)
        {
            if (oldCard.upgraded)
            {
                newCard.upgrade();
            }

            newCard.current_x = oldCard.current_x;
            newCard.current_y = oldCard.current_y;
            newCard.target_x = oldCard.target_x;
            newCard.target_y = oldCard.target_y;

            player.hand.group.remove(index);
            player.hand.group.add(index, newCard);
            player.hand.glowCheck();
        }
    }

    private static void SetNewCardToTransform(AbstractCard oldCard, boolean isRandom, String sourceName, String cardSelectDescription)
    {
        newCard = null;
        int cost = oldCard.cost;

        if (cardPool == null) {
            GenerateCardPool();
        }

        CardGroup currentGroup = cardPool.get(cost);

        if (currentGroup == null)
        {
            return;
        }

        currentGroup.removeCard(oldCard);

        if (currentGroup.isEmpty())
        {
            return;
        }

        if (isRandom)
        {
            newCard = currentGroup.getRandomCard(true);
        }
        else
        {
            GameActions.Bottom.SelectFromPile(sourceName, 1, currentGroup)
                    .SetMessage(cardSelectDescription)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            newCard = cards.get(0);
                        }
                    });
        }
    }

    private static void GenerateCardPool()
    {
        cardPool = new Hashtable();

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS &&
                    c instanceof AnimatorCard && !c.tags.contains(AbstractCard.CardTags.HEALING)
                    && c.rarity != AbstractCard.CardRarity.BASIC) {
                AddCardToPoolByCost(c);
            }
        }
    }

    private static void AddCardToPoolByCost(AbstractCard card)
    {
        int cost = card.cost;

        CardGroup group = cardPool.get(cost);

        if (cardPool.get(cost) == null)
        {
            group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
            cardPool.put(cost, group);
        }

        group.addToTop(card);
    }


}