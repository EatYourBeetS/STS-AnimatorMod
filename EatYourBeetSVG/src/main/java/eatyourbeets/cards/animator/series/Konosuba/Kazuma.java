package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Kazuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuma.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Kazuma()
    {
        super(DATA);

        Initialize(0, 6, 2);
        SetUpgrade(0, 2, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Orange(1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (HasSynergy() ? magicNumber : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, 1);
    }
}