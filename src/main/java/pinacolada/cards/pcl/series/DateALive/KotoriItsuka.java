package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KotoriItsuka extends PCLCard
{
    public static final PCLCardData DATA = Register(KotoriItsuka.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Fire).SetSeriesFromClassPackage();
    public static final int THRESHOLD = 12;
    public static final int BURNING_ATTACK_BONUS = 10;

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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);
        PCLActions.Bottom.ApplyBurning(player, m, info.IsSynergizing ? secondaryValue + 1 : secondaryValue);
        if (PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber) {
            PCLActions.Bottom.AddPowerEffectEnemyBonus(BurningPower.POWER_ID, BURNING_ATTACK_BONUS);
        }
    }
}