package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.List;

public class GirlDeMo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GirlDeMo.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
        SetExhaust(true);
        SetEthereal(true);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Callback(() -> {
            List<String> highestPower = getHighestPowers(p);

            int powerValue;

            if (highestPower.contains(ForcePower.POWER_ID))
            {
                powerValue = GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID);

                if (powerValue > 0)
                {
                    GameActions.Bottom.GainForce(powerValue);
                }
            }
            if (highestPower.contains(AgilityPower.POWER_ID))
            {
                powerValue = GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID);

                if (powerValue > 0)
                {
                    GameActions.Bottom.GainAgility(powerValue);
                }
            }
            if (highestPower.contains(IntellectPower.POWER_ID))
            {
                powerValue = GameUtilities.GetPowerAmount(p, IntellectPower.POWER_ID);

                if (powerValue > 0)
                {
                    GameActions.Bottom.GainIntellect(powerValue);
                }
            }
        });
    }

    private List<String> getHighestPowers(AbstractPlayer p)
    {
        int forceAmount = GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID);
        int agilityAmount = GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID);
        int intellectAmount = GameUtilities.GetPowerAmount(p, IntellectPower.POWER_ID);

        List<String> highestPowers = new ArrayList<>();

        if (forceAmount == agilityAmount && agilityAmount == intellectAmount)
        {
            highestPowers.add(ForcePower.POWER_ID);
            highestPowers.add(AgilityPower.POWER_ID);
            highestPowers.add(IntellectPower.POWER_ID);
        }
        else if (forceAmount == agilityAmount && forceAmount > intellectAmount)
        {
            highestPowers.add(ForcePower.POWER_ID);
            highestPowers.add(AgilityPower.POWER_ID);
        }
        else if (forceAmount == intellectAmount && forceAmount > agilityAmount)
        {
            highestPowers.add(ForcePower.POWER_ID);
            highestPowers.add(IntellectPower.POWER_ID);
        }
        else if (agilityAmount == intellectAmount && agilityAmount > forceAmount)
        {
            highestPowers.add(AgilityPower.POWER_ID);
            highestPowers.add(IntellectPower.POWER_ID);
        }
        else if (forceAmount > agilityAmount && forceAmount > intellectAmount)
        {
            highestPowers.add(ForcePower.POWER_ID);
        }
        else if (agilityAmount > forceAmount && agilityAmount > intellectAmount)
        {
            highestPowers.add(AgilityPower.POWER_ID);
        }
        else if (intellectAmount > agilityAmount && intellectAmount > forceAmount)
        {
            highestPowers.add(IntellectPower.POWER_ID);
        }

        return highestPowers;
    }
}