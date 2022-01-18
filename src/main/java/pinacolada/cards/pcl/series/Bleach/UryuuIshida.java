package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class UryuuIshida extends PCLCard {
    public static final PCLCardData DATA = Register(UryuuIshida.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public UryuuIshida() {
        super(DATA);

        Initialize(4, 0, 1, 2);
        SetUpgrade(2, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Silver(1, 0 ,0);
        SetAffinity_Orange(0, 0, 1);
    }

    @Override
    public void triggerOnManualDiscard() {
        PCLActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (IsStarter()) {
            PCLActions.Bottom.Callback(m, (enemy, __) -> TransferWeakVulnerable(enemy));
        }
    }

    private void TransferWeakVulnerable(AbstractMonster m) {
        int weakToTransfer = PCLGameUtilities.GetPowerAmount(player, WeakPower.POWER_ID);
        int vulToTransfer = PCLGameUtilities.GetPowerAmount(player, VulnerablePower.POWER_ID);

        if (weakToTransfer > magicNumber) {
            weakToTransfer = magicNumber;
        }
        if (vulToTransfer > magicNumber) {
            vulToTransfer = magicNumber;
        }

        for (AbstractPower power : player.powers) {
            if (WeakPower.POWER_ID.equals(power.ID) && weakToTransfer > 0) {
                PCLActions.Bottom.ReducePower(power, weakToTransfer);
                PCLActions.Bottom.ApplyWeak(player, m, weakToTransfer);
            } else if (VulnerablePower.POWER_ID.equals(power.ID) && vulToTransfer > 0) {
                PCLActions.Bottom.ReducePower(power, vulToTransfer);
                PCLActions.Bottom.ApplyVulnerable(player, m, vulToTransfer);
            }
        }
    }
}