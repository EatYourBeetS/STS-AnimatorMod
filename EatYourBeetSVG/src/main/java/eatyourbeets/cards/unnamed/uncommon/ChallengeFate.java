package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class ChallengeFate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(ChallengeFate.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .ObtainableAsReward((data, deck) -> deck.size() >= (14 + (8 * data.GetTotalCopies(deck))));

    public ChallengeFate()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ExhaustFromHand(name, 1, true).ShowEffect(true, true);
    }
}