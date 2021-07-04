package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Noelle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noelle.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self);

    public Noelle()
    {
        super(DATA);

        Initialize(0, 5, 4);
        SetUpgrade(0, 2, 1);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (HasSynergy())
        {
            amount += Math.min(GameUtilities.GetTeamwork(null), magicNumber);
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            AbstractOrb firstOrb = null;
            for (AbstractOrb orb : p.orbs)
            {
                if (Earth.ORB_ID.equals(orb.ID))
                {
                    firstOrb = orb;
                    break;
                }
            }

            if (firstOrb != null) {
                firstOrb.onStartOfTurn();
                firstOrb.onEndOfTurn();
            }
            else
            {
                GameActions.Bottom.ChannelOrb(new Earth());
            }
        }
    }
}