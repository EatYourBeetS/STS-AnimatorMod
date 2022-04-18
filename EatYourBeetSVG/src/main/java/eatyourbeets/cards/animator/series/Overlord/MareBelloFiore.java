package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.ColoredString;
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

        Initialize(0, 0, 6, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Blue, 2);
        SetAffinityRequirement(Affinity.Green, 2);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return gainTempHP ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(WEAK);
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
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(CheckAffinity(Affinity.Green) ? EYBCardTarget.Normal : EYBCardTarget.None);
        gainTempHP = CheckAffinity(Affinity.Blue);
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
            GameActions.Bottom.ApplyWeak(p, m, WEAK);
        }

        GameActions.Bottom.ChannelOrbs(Earth::new, secondaryValue);
        GameActions.Bottom.TriggerOrbPassive(player.orbs.size())
        .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
        .SetSequential(true);
    }
}