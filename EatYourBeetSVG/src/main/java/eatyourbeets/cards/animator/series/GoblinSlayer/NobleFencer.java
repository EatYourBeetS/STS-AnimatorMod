package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NobleFencer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NobleFencer.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public NobleFencer()
    {
        super(DATA);

        Initialize(0, 2, 3);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = GameUtilities.GetFirstOrb(Lightning.ORB_ID);
        if (orb != null)
        {
            orb.showEvokeValue();
        }
    }

    @Override
    public void triggerOnAffinitySeal(boolean manual)
    {
        super.triggerOnAffinitySeal(manual);
        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainIntellect(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.EvokeOrb(1)
                .SetFilter(o -> Lightning.ORB_ID.equals(o.ID))
                .AddCallback(orbs ->
                {
                    if (orbs.size() > 0)
                    {
                        GameActions.Bottom.GainAgility(1);
                        GameActions.Bottom.GainIntellect(1);
                    }
                    else
                    {
                        GameActions.Bottom.ChannelOrbs(Lightning::new, secondaryValue);
                    }
                });
    }
}