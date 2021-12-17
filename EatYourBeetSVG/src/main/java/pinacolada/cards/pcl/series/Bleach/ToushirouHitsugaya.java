package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ToushirouHitsugaya extends PCLCard
{
    public static final PCLCardData DATA = Register(ToushirouHitsugaya.class).SetAttack(1, CardRarity.RARE, PCLAttackType.Normal).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ToushirouHitsugaya_Bankai(), false));
    public static final int FREEZING_THRESHOLD = 8;

    public ToushirouHitsugaya() {
        super(DATA);

        Initialize(6, 3, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Red(1,0,1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), magicNumber).ShowEffect(true, true).AddCallback(m, (enemy, __) -> {
            int freezingAmount = magicNumber;
            if (PCLGameUtilities.GetPowerAmount(m, BurningPower.POWER_ID) > 0) {
                PCLActions.Bottom.ReducePower(player, enemy, BurningPower.POWER_ID, secondaryValue);
                PCLActions.Bottom.ApplyFreezing(TargetHelper.Normal(enemy), magicNumber).ShowEffect(true, true);
            }
        });

        PCLActions.Bottom.Callback(m, (enemy, __) -> {
            if (PCLGameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) > FREEZING_THRESHOLD) {
                PCLActions.Bottom.MakeCardInDrawPile(new ToushirouHitsugaya_Bankai());
                PCLActions.Bottom.Exhaust(this);
            }
        });
    }
}