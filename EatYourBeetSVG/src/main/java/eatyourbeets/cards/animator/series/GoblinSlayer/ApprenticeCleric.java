package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.HashMap;
import java.util.UUID;

public class ApprenticeCleric extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ApprenticeCleric.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public ApprenticeCleric()
    {
        super(DATA);

        Initialize(0, 0, 8, 2);
        SetUpgrade(0,0,2,2);

        SetAffinity_Light();

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RaiseLightLevel(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        int hindranceThreshold = 0;

        for (AbstractCard card : p.exhaustPile.group)
        {
            if (GameUtilities.IsHindrance(card))
            {
                hindranceThreshold++;
            }
        }

        if (hindranceThreshold > 3)
        {
            RandomizedList<AnimatorCard> cardsInHand = new RandomizedList<>();

            for (AbstractCard card : player.hand.group)
            {
                if (card instanceof AnimatorCard)
                {
                    cardsInHand.Add((AnimatorCard) card);
                }
            }

            for (int i=0; i<secondaryValue; i++)
            {
                if (cardsInHand.Size() <= 0)
                {
                    break;
                }

                AnimatorCard card = cardsInHand.Retrieve(rng);

                if (card != null)
                {
                    GameUtilities.GainAffinity(card, Affinity.Light);
                }
            }

        }
    }
}