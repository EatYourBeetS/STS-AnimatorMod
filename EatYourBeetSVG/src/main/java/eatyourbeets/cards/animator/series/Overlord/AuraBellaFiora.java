package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
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

        Initialize(0, 4, 3);
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
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Delayed.Motivate().SetFilter(GameUtilities::IsHighCost);
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false);
    }
}