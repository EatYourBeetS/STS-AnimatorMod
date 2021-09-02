package eatyourbeets.cards.animator.series.Overlord;

import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class AuraBellaFiora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AuraBellaFiora.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public AuraBellaFiora()
    {
        super(DATA);

        Initialize(0, 3, 3);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1);
        SetAffinity_Red(1);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (IsStarter() ? magicNumber : 0);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, BaseMod.MAX_HAND_SIZE, false)
        .SetOptions(false, false, true)
        .SetFilter(GameUtilities::IsLowCost);
        GameActions.Delayed.Motivate(1);
    }
}