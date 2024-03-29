package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EmiyaShirou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EmiyaShirou.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public EmiyaShirou()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 1, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
}