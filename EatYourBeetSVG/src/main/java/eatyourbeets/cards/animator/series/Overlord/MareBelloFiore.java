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

    public MareBelloFiore()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 2, 1);

        SetAffinity_Blue(2);
        SetAffinity_Green(1);
        SetAffinity_Orange(1);

        SetAffinityRequirement(Affinity.Blue, 2);
        SetAffinityRequirement(Affinity.Green, 2);

        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && CheckAffinity(Affinity.Green))
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(CheckAffinity(Affinity.Green) ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }
        if (CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.ApplyWeak(p, m, secondaryValue);
        }

        GameActions.Bottom.ChannelOrb(new Earth());
        GameActions.Bottom.TriggerOrbPassive(player.orbs.size())
        .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
        .SetSequential(true);
    }
}