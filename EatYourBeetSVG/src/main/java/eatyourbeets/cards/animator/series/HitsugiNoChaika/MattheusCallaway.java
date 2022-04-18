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

        Initialize(0, 5, 3);
        SetUpgrade(0, 0, 3);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + ((GameActionManager.totalDiscardedThisTurn > 0) ? magicNumber : 0);
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
            if (player.filledOrbCount() >= player.maxOrbs && info2.TryActivateLimited())
            {
                GameActions.Bottom.GainOrbSlots(1);
                GameActions.Bottom.ChannelOrb(new Earth());
            }
        });
    }
}