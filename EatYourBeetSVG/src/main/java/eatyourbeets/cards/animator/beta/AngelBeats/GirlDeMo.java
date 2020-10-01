package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;

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
        List<String> highestPower = getHighestPowers(p);

        int powerValue;

        if (highestPower.contains(StrengthPower.POWER_ID))
        {
            powerValue = p.getPower(StrengthPower.POWER_ID).amount;

            if (powerValue > 0)
            {
                GameActions.Bottom.GainForce(powerValue);
            }
        }
        if (highestPower.contains(AgilityPower.POWER_ID))
        {
            powerValue = p.getPower(AgilityPower.POWER_ID).amount;

            if (powerValue > 0)
            {
                GameActions.Bottom.GainAgility(powerValue);
            }
        }
        if (highestPower.contains(IntellectPower.POWER_ID))
        {
            powerValue = p.getPower(IntellectPower.POWER_ID).amount;

            if (powerValue > 0)
            {
                GameActions.Bottom.GainIntellect(powerValue);
            }
        }
    }

    private List<String> getHighestPowers(AbstractPlayer p)
    {
        int strengthAmount = p.getPower(StrengthPower.POWER_ID).amount;
        int agilityAmount = p.getPower(AgilityPower.POWER_ID).amount;
        int intellectAmount = p.getPower(IntellectPower.POWER_ID).amount;

        List<String> highestPowers = new ArrayList<>();

        if (strengthAmount == agilityAmount && agilityAmount == intellectAmount)
        {
            highestPowers.add(StrengthPower.POWER_ID);
            highestPowers.add(AgilityPower.POWER_ID);
            highestPowers.add(IntellectPower.POWER_ID);
        }
        else if (strengthAmount == agilityAmount && strengthAmount > intellectAmount)
        {
            highestPowers.add(StrengthPower.POWER_ID);
            highestPowers.add(AgilityPower.POWER_ID);
        }
        else if (strengthAmount == intellectAmount && strengthAmount > agilityAmount)
        {
            highestPowers.add(StrengthPower.POWER_ID);
            highestPowers.add(IntellectPower.POWER_ID);
        }
        else if (agilityAmount == intellectAmount && agilityAmount > strengthAmount)
        {
            highestPowers.add(AgilityPower.POWER_ID);
            highestPowers.add(IntellectPower.POWER_ID);
        }
        else if (strengthAmount > agilityAmount && strengthAmount > intellectAmount)
        {
            highestPowers.add(StrengthPower.POWER_ID);
        }
        else if (agilityAmount > strengthAmount && agilityAmount > intellectAmount)
        {
            highestPowers.add(AgilityPower.POWER_ID);
        }
        else if (intellectAmount > agilityAmount && intellectAmount > strengthAmount)
        {
            highestPowers.add(IntellectPower.POWER_ID);
        }

        return highestPowers;
    }
}