package eatyourbeets.cards.animator.beta.Bleach;

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

public class RukiaBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RukiaBankai.class).SetSkill(-1, CardRarity.SPECIAL, EYBCardTarget.None);

    public RukiaBankai()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);
        SetExhaust(true);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);

        for (int i=0; i<stacks; i++)
        {
            AbstractOrb frost = null;

            for (AbstractOrb orb : player.orbs)
            {
                if (Frost.ORB_ID.equals(orb.ID))
                {
                    frost = orb;
                    break;
                }
            }

            if (frost != null)
            {
                GameActions.Bottom.EvokeOrb(magicNumber);
            }
            else
            {
                break;
            }
        }
    }
}