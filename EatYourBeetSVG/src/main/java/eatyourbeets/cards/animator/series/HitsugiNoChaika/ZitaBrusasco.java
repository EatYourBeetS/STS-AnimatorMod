package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class ZitaBrusasco extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ZitaBrusasco.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public ZitaBrusasco()
    {
        super(DATA);

        Initialize(0, 3);
        SetUpgrade(0, 2);

        SetAffinity_Light(1);
        SetAffinity_Blue(1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (info.TryActivateStarter())
        {
            GameActions.Bottom.GainIntellect(1);
        }

        GameActions.Bottom.ChannelOrb(CheckSpecialCondition(true) ? new Frost() : new Lightning());
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        int frost = 0;
        int lightning = 0;
        for (AbstractOrb orb : player.orbs)
        {
            if (Frost.ORB_ID.equals(orb.ID))
            {
                frost += 1;
            }
            else if (Lightning.ORB_ID.equals(orb.ID))
            {
                lightning += 1;
            }
        }

        return frost < lightning;
    }
}