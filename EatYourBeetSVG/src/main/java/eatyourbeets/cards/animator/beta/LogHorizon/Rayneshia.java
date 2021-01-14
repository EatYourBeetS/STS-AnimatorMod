package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Rayneshia extends AnimatorCard {
    public static final EYBCardData DATA = Register(Rayneshia.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    protected static final ArrayList<AbstractCard> synergicCards = new ArrayList();

    public Rayneshia() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AddCardsFromGroupToSynergy(player.drawPile);
        DrawSynergicCards(player.drawPile);
    }

    @Override
    public boolean HasSynergy(AbstractCard other)
    {
        return (other.cost == 2) || (other.isEthereal) || super.HasSynergy(other);
    }

    private void AddCardsFromGroupToSynergy(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (HasSynergy(c))
            {
                synergicCards.add(c);
            }
        }
    }

    private void DrawSynergicCards(CardGroup group)
    {
        RandomizedList<AbstractCard> randomizedSynergicCards = new RandomizedList<>(synergicCards);

        for (int i=0; i<magicNumber; i++)
        {
            if (i > randomizedSynergicCards.Size())
            {
                break;
            }

            AbstractCard randomCard = randomizedSynergicCards.Retrieve(rng, true);

            if (randomCard != null)
            {
                GameActions.Top.MoveCard(randomCard, group, player.hand)
                        .ShowEffect(true, true);
            }
        }
    }
}