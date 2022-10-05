package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shiro extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shiro.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);

        SetAffinity_Blue(2);
        SetAffinity_Light(1, 1, 0);

        SetAffinityRequirement(Affinity.Blue, 5);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1, upgraded);
        GameActions.Bottom.Motivate(1);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Callback(() ->
            {
//                GameActions.Bottom.ReducePower(player, StrengthPower.POWER_ID, 1);
//                GameActions.Bottom.ReducePower(player, DexterityPower.POWER_ID, 1);
//                GameActions.Bottom.ReducePower(player, FocusPower.POWER_ID, 1);
                GameActions.Bottom.SFX(SFX.TURN_EFFECT, 0.75f, 0.75f);
                GameUtilities.ModifyCardDrawPerTurn(-1, 1);
                GameUtilities.ModifyEnergyGainPerTurn(secondaryValue, 1);
            });
        }
    }
}