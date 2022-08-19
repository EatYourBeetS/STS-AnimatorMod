package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EmiyaShirou extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(EmiyaShirou.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public EmiyaShirou()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 1, 1);

        SetSeries(CardSeries.Fate);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.Add(new RandomCardUpgrade());
        }

        boolean fullyUpgraded = true;
        for (AbstractCard card : p.hand.group)
        {
            if (card != this && !card.upgraded && !GameUtilities.IsHindrance(card))
            {
                fullyUpgraded = false;
                break;
            }
        }

        if (fullyUpgraded)
        {
            GameActions.Bottom.Draw(2);
        }
    }
}