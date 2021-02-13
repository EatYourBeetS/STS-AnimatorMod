package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class RukiaBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RukiaBankai.class).SetSkill(-1, CardRarity.SPECIAL, EYBCardTarget.None);

    public RukiaBankai()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);
        SetExhaust(true);
        SetMultiDamage(true);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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
        }
    }
}