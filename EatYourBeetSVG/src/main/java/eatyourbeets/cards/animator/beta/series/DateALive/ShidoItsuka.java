package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class ShidoItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShidoItsuka.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    protected final static ArrayList<AbstractCard> dateALiveCards = new ArrayList<>();
    protected final static ArrayList<AbstractCard> otherSynergicCards = new ArrayList<>();

    public ShidoItsuka()
    {
        super(DATA);

        Initialize(0, 8, 3);
        SetUpgrade(0, 0);
        SetAffinity_Blue(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return (other instanceof AnimatorCard && ((AnimatorCard) other).series.Equals(CardSeries.DateALive));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        InitializeSynergicCards();

        RandomizedList<AbstractCard> randomizedDALCards = new RandomizedList<>(dateALiveCards);
        RandomizedList<AbstractCard> randomizedSynergicCards = new RandomizedList<>(otherSynergicCards);

        final CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        for (int i = 0; i < magicNumber; i++)
        {
            AbstractCard randomCard;
            if (i == 0 && rng.randomBoolean(0.4f))
            {
                //40% chance the first option is a non-Date-a-Live card such as a shapeshifter
                if (randomizedSynergicCards.Size() == 0)
                {
                    break;
                }

                randomCard = randomizedSynergicCards.Retrieve(rng, true).makeCopy();
            }
            else
            {
                if (randomizedDALCards.Size() == 0)
                {
                    break;
                }

                randomCard = randomizedDALCards.Retrieve(rng, true).makeCopy();
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
                        if (upgraded)
                        {
                            GameActions.Bottom.MakeCardInDrawPile(cards.get(0))
                                    .SetDuration(Settings.ACTION_DUR_FASTER, true);
                        }
                        else
                        {
                            GameActions.Bottom.MakeCardInDiscardPile(cards.get(0))
                                    .SetDuration(Settings.ACTION_DUR_FASTER, true);
                        }
                    }
                });

        if (info.IsSynergizing && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Last.ModifyAllInstances(uuid, c -> ((EYBCard) c).SetExhaust(true));
        }
    }

    private void InitializeSynergicCards()
    {
        dateALiveCards.clear();
        otherSynergicCards.clear();

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c instanceof AnimatorCard && !GameUtilities.IsHindrance(c)
            && !c.hasTag(AbstractCard.CardTags.HEALING)
            && c.rarity != AbstractCard.CardRarity.SPECIAL
            && c.rarity != AbstractCard.CardRarity.BASIC)
            {
                if (WouldSynergize(c))
                {
                    if (((AnimatorCard) c).series == CardSeries.DateALive)
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