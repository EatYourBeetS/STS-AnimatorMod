package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.List;

public class ShidoItsuka extends AnimatorCard {
    public static final EYBCardData DATA = Register(ShidoItsuka.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);
    private static final ArrayList<AbstractCard> dateALiveCards = new ArrayList<>();
    private static final ArrayList<AbstractCard> otherSynergicCards = new ArrayList<>();

    public ShidoItsuka() {
        super(DATA);

        Initialize(0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numOptions = 2;

        if (HasSynergy())
        {
            numOptions = 3;
        }

        if (dateALiveCards.isEmpty() || otherSynergicCards.isEmpty())
        {
            InitializeSynergicCards();
        }

        RandomizedList<AbstractCard> randomizedDALCards = new RandomizedList(dateALiveCards);
        RandomizedList<AbstractCard> randomizedSynergicCards = new RandomizedList(otherSynergicCards);
        CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        for (int i=0; i<numOptions; i++)
        {
            AbstractCard randomCard;

            if (i == 0 && AbstractDungeon.cardRandomRng.random(10) < 4)
            {
                //40% chance the first option is a non-Date-a-Live card such as a shapeshifter
                randomCard = randomizedSynergicCards.Retrieve(AbstractDungeon.cardRandomRng, true);
            }
            else
            {
                randomCard = randomizedDALCards.Retrieve(AbstractDungeon.cardRandomRng, true);
            }

            if (upgraded)
            {
                randomCard.upgrade();
            }
            options.addToBottom(randomCard);
        }

        GameActions.Top.SelectFromPile(name, 1, options)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.MakeCard(cards.get(0), p.drawPile).SetDuration(Settings.ACTION_DUR_FASTER, true);
            }
        });
    }

    private void InitializeSynergicCards()
    {
        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS &&
            c instanceof AnimatorCard && !c.tags.contains(AbstractCard.CardTags.HEALING)
            && c.rarity != AbstractCard.CardRarity.BASIC && c.rarity != AbstractCard.CardRarity.SPECIAL) {
                if (Synergies.WouldSynergize(this, c)) {
                    if (((AnimatorCard) c).synergy == Synergies.DateALive)
                    {
                        dateALiveCards.add(c);
                    }
                    else
                     {
                        otherSynergicCards.add(c);
                    }
                }
            }
        }
    }
}