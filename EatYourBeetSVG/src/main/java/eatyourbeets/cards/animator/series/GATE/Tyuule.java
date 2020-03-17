package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tyuule extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tyuule.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL);
    public static final EYBCardTooltip CommonDebuffs = new EYBCardTooltip(DATA.Strings.EXTENDED_DESCRIPTION[0], DATA.Strings.EXTENDED_DESCRIPTION[1]);

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(CommonDebuffs);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.ApplyPoison(p, enemy, magicNumber).AddCallback(enemy, (e, __) ->
            {
                AbstractMonster target = (AbstractMonster)e;
                for (AbstractPower power : target.powers)
                {
                    if (WeakPower.POWER_ID.equals(power.ID))
                    {
                        GameActions.Bottom.ApplyWeak(player, target, 1);
                    }
                    else if (VulnerablePower.POWER_ID.equals(power.ID))
                    {
                        GameActions.Bottom.ApplyVulnerable(player, target, 1);
                    }
                    else if (PoisonPower.POWER_ID.equals(power.ID))
                    {
                        GameActions.Bottom.ApplyPoison(player, target, 1);
                    }
                    else if (BurningPower.POWER_ID.equals(power.ID))
                    {
                        GameActions.Bottom.ApplyBurning(player, target, 1);
                    }
                    else if (GainStrengthPower.POWER_ID.equals(power.ID))
                    {
                        GameActions.Bottom.ReduceStrength(target, 1, true);
                    }
                }
            });
        }
    }
}