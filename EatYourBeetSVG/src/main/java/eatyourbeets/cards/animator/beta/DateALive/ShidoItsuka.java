package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class ShidoItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShidoItsuka.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);
    private ArrayList<AbstractCard> dateALiveCards = new ArrayList<>();
    private ArrayList<AbstractCard> otherSynergicCards = new ArrayList<>();

    public ShidoItsuka()
    {
        super(DATA);

        Initialize(0, 5);
        SetUpgrade(0, 2);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (HasSynergy() && !EffectHistory.HasActivatedLimited(cardID))
        {
            SetExhaust(false);
        }
        else
        {
            SetExhaust(true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        InitializeSynergicCards();

        RandomizedList<AbstractCard> randomizedDALCards = new RandomizedList<>(dateALiveCards);
        RandomizedList<AbstractCard> randomizedSynergicCards = new RandomizedList<>(otherSynergicCards);

        final int numOptions = 3;
        final CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        for (int i = 0; i < numOptions; i++)
        {
            AbstractCard randomCard;
            if (i == 0 && rng.randomBoolean(0.4f))
            {
                //40% chance the first option is a non-Date-a-Live card such as a shapeshifter
                randomCard = randomizedSynergicCards.Retrieve(rng, true).makeCopy();
            }
            else
            {
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
                GameActions.Bottom.MakeCardInDiscardPile(cards.get(0))
                .SetDuration(Settings.ACTION_DUR_FASTER, true);
            }
        });

        if (HasSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Last.ModifyAllCombatInstances(uuid, c -> ((EYBCard)c).SetExhaust(true));
        }
    }

    private void InitializeSynergicCards()
    {
        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS
            && c instanceof AnimatorCard && !c.tags.contains(AbstractCard.CardTags.HEALING)
            && c.rarity != AbstractCard.CardRarity.BASIC && c.rarity != AbstractCard.CardRarity.SPECIAL)
            {
                if (Synergies.WouldSynergize(this, c))
                {
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