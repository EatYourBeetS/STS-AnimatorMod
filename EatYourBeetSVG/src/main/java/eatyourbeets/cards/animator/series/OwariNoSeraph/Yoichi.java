package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Yoichi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yoichi.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph);

    public Yoichi()
    {
        super(DATA);

        Initialize(0,0,1);
        SetUpgrade(0,0,1);

        SetAffinity_Green(1);
        SetAffinity_Light(1, 1, 0);

        SetAffinityRequirement(Affinity.Green, 1);
        SetAffinityRequirement(Affinity.Light, 1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false);
        GameActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber))
        .AddCallback(info, (info2, power) ->
        {
            if (CheckSpecialCondition(true))
            {
                final SupportDamagePower supportDamage = JUtils.SafeCast(power, SupportDamagePower.class);
                if (supportDamage != null)
                {
                    supportDamage.atEndOfTurn(true);
                }
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return super.CheckSpecialConditionSemiLimited(tryUse, super::CheckSpecialCondition);
    }
}