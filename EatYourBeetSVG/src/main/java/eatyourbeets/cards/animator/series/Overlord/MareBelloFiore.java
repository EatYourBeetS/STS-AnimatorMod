package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MareBelloFiore extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MareBelloFiore.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int WEAK = 2;

    protected boolean gainTempHP = false;

    public MareBelloFiore()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);

        SetAffinity_Blue(1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Blue, 2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
        SetFading(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(CheckSpecialCondition(false) ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Earth::new, secondaryValue);
        GameActions.Bottom.TriggerOrbPassive(player.orbs.size())
        .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
        .SetSequential(true);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ApplyWeak(p, m, WEAK);
        }
    }
}