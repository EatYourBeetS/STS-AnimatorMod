package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MeguKakizaki extends PCLCard
{
    public static final PCLCardData DATA = Register(MeguKakizaki.class)
    		.SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None).SetSeriesFromClassPackage();
    
    public MeguKakizaki()
    {
        super(DATA);

        Initialize(0, 8, 6, 3);
        SetUpgrade(0, 2, 5, 0);
        SetAffinity_Light(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Dark, 5);

        SetEthereal(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        heal = PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) > 0 ? block : 0;
    }

    @Override
    public void triggerOnExhaust()
    {
        PCLActions.Bottom.Callback(() ->
        {
            if (CheckAffinity(PCLAffinity.Dark)) {
                PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), secondaryValue);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block).AddCallback(() -> {
            int toTransfer = Math.min(magicNumber, PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID));
            PCLActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, toTransfer);
            AbstractMonster mo = PCLGameUtilities.GetRandomEnemy(true);
            if (mo != null && toTransfer > 0) {
                PCLActions.Bottom.DealDamageAtEndOfTurn(player, mo, toTransfer, AttackEffects.CLAW);
            }
        });
    }
}

