package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class EmiyaShirou extends AnimatorCard
{
    public static final String ID = Register(EmiyaShirou.class.getSimpleName(), EYBCardBadge.Special);

    public EmiyaShirou()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0,5, 2);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(block);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.Add(new RandomCardUpgrade());
        }

        boolean fullyUpgraded = true;
        for (AbstractCard card : p.hand.group)
        {
            if (card != this && !card.upgraded && !GameUtilities.IsCurseOrStatus(card))
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

    @Override
    public void upgrade() 
    {
        if (TryUpgrade(false))
        {
            upgradeBlock(1);
            upgradeMagicNumber(1);
        }
    }
}