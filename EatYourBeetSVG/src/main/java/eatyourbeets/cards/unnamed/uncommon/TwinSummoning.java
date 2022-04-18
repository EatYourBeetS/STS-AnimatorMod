package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class TwinSummoning extends UnnamedCard
{
    public static final EYBCardData DATA = Register(TwinSummoning.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .ObtainableAsReward((data, deck) -> deck.size() >= (14 + (8 * data.GetTotalCopies(deck))));

    public TwinSummoning()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);

        SetSummon(true);
        SetExhaust(true);
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int count = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c.uuid != uuid)
            {
                count += 1;
            }
        }

        SetUnplayable(count < magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false);
        GameActions.Bottom.SummonDoll(secondaryValue).SameType(true);
    }
}