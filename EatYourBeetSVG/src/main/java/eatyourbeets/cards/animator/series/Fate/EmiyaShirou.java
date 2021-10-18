package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EmiyaShirou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EmiyaShirou.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    private Fire fireOrb;

    public EmiyaShirou()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 1, 1);

        SetAffinity_Light();

        SetProtagonist(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Add(new RandomCardUpgrade());
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
            GameActions.Bottom.FetchFromPile(name, 1, p.drawPile)
                    .SetFilter(card -> GameUtilities.HasAffinity(card, Affinity.Light));
        }
    }
}