package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.IkkakuBankai;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class IkkakuMadarame extends PCLCard {
    public static final PCLCardData DATA = Register(IkkakuMadarame.class).SetAttack(2, CardRarity.COMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new ZarakiKenpachi(), false);
                data.AddPreview(new IkkakuBankai(), false);
            });

    public IkkakuMadarame() {
        super(DATA);

        Initialize(6, 1, 2, 5);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);
        PCLActions.Bottom.GainBlock(block);

        PCLGameUtilities.MaintainPower(PCLAffinity.Green);

        if (CheckSpecialCondition(true)) {
            PCLActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
            PCLActions.Last.ModifyAllInstances(uuid).AddCallback(PCLActions.Bottom::Exhaust);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Red) >= secondaryValue || PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Green) >= secondaryValue;
    }
}