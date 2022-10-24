package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class MattheusCallaway extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MattheusCallaway.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public MattheusCallaway()
    {
        super(DATA);

        Initialize(0, 5, 5);
        SetUpgrade(0, 2, 0);

        SetAffinity_Green(1, 1, 0);
        SetAffinity_Blue(1, 1, 0);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (GameActionManager.totalDiscardedThisTurn > 0 ? magicNumber : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Delayed.Callback(info, (info2, __) ->
        {
            if (CheckSpecialCondition(true))
            {
                GameActions.Bottom.GainOrbSlots(1);
                GameActions.Bottom.ChannelOrb(new Earth());
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return super.CheckSpecialConditionLimited(tryUse, __ -> player.maxOrbs == player.filledOrbCount());
    }
}