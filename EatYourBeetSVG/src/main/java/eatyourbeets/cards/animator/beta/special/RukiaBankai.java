package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class RukiaBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RukiaBankai.class).SetSkill(-1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.Bleach);

    public RukiaBankai()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Earth(1, 0, 0);
        SetAffinity_Air(1, 1, 0);
        SetExhaust(true);
        SetMultiDamage(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);

        int frostExhaustCount = 0;
        ArrayList<AbstractOrb> frostsToExhaust = new ArrayList<>();

        for (AbstractOrb orb : player.orbs)
        {
            if (Frost.ORB_ID.equals(orb.ID))
            {
                frostsToExhaust.add(orb);
                frostExhaustCount++;

                if (frostExhaustCount >= stacks)
                {
                    break;
                }
            }
        }

        for (AbstractOrb orb : frostsToExhaust)
        {
            GameActions.Bottom.EvokeOrb(magicNumber, orb);
            GameActions.Bottom.ApplyFreezing(TargetHelper.RandomEnemy(), secondaryValue);
        }
    }
}