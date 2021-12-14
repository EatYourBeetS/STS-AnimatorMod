package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KotoriItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriItsuka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();
    public static final int THRESHOLD = 12;
    public static final int BURNING_ATTACK_BONUS = 5;

    public KotoriItsuka()
    {
        super(DATA);

        Initialize(8, 0, 3, 2);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(0, 0, 1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) >= magicNumber)
        {
            return super.ModifyDamage(enemy, amount * 2);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d.AddCallback(m, (enemy, __) -> {
            if (GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) >= 1)
            {
                GameActions.Bottom.ReducePower(player, enemy, FreezingPower.POWER_ID, magicNumber);
            }

            if (info.IsSynergizing) {
                GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(BurningPower.POWER_ID, BURNING_ATTACK_BONUS));
            }
        }));
        GameActions.Bottom.ApplyBurning(player, m, secondaryValue);
    }
}