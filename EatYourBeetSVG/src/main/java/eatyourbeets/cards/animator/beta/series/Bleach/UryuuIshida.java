package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UryuuIshida extends AnimatorCard {
    public static final EYBCardData DATA = Register(UryuuIshida.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public UryuuIshida() {
        super(DATA);

        Initialize(4, 0, 1, 2);
        SetUpgrade(2, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void triggerOnManualDiscard() {
        GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (IsStarter()) {
            GameActions.Bottom.Callback(m, (enemy, __) -> TransferWeakVulnerable(enemy));
        }
    }

    private void TransferWeakVulnerable(AbstractMonster m) {
        int weakToTransfer = GameUtilities.GetPowerAmount(player, WeakPower.POWER_ID);
        int vulToTransfer = GameUtilities.GetPowerAmount(player, VulnerablePower.POWER_ID);

        if (weakToTransfer > magicNumber) {
            weakToTransfer = magicNumber;
        }
        if (vulToTransfer > magicNumber) {
            vulToTransfer = magicNumber;
        }

        for (AbstractPower power : player.powers) {
            if (WeakPower.POWER_ID.equals(power.ID) && weakToTransfer > 0) {
                GameActions.Bottom.ReducePower(power, weakToTransfer);
                GameActions.Bottom.ApplyWeak(player, m, weakToTransfer);
            } else if (VulnerablePower.POWER_ID.equals(power.ID) && vulToTransfer > 0) {
                GameActions.Bottom.ReducePower(power, vulToTransfer);
                GameActions.Bottom.ApplyVulnerable(player, m, vulToTransfer);
            }
        }
    }
}