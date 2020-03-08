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
import eatyourbeets.utilities.GameUtilities;

import java.util.Dictionary;
import java.util.Hashtable;

public class Natsumi extends AnimatorCard implements Spellcaster {
    public static final EYBCardData DATA = Register(Natsumi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    private static Dictionary<Integer, CardGroup> cardPool;

    public Natsumi() {
        super(DATA);

        Initialize(0, 0);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
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
                int powerCount = player.discardPile.getCardsOfType(CardType.POWER).size() + player.drawPile.getCardsOfType(CardType.POWER).size()
                            + player.hand.getCardsOfType(CardType.POWER).size();

                TransformCard(p, cards.get(0), powerCount >= 7);
            }
        });
    }

    private void TransformCard(AbstractPlayer player, AbstractCard oldCard, boolean isRandom)
    {
        int cost = oldCard.cost;

        if (cardPool == null) {
            GenerateCardPool();
        }

        CardGroup currentGroup = cardPool.get(cost);

        if (currentGroup == null || currentGroup.isEmpty())
        {
            return;
        }

        if (isRandom)
        {
            GameActions.Bottom.ReplaceCard(oldCard.uuid, currentGroup.getRandomCard(true).makeCopy()).SetUpgrade(upgraded);
        }
        else
        {
            GameActions.Bottom.SelectFromPile(name, 1, currentGroup)
            .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[1])
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameActions.Bottom.ReplaceCard(oldCard.uuid, cards.get(0).makeCopy()).SetUpgrade(upgraded);
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
                    && c.rarity != AbstractCard.CardRarity.BASIC && c.rarity != AbstractCard.CardRarity.SPECIAL) {
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