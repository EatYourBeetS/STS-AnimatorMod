package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.replacement.PCLVulnerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Mayuri extends PCLCard
{
    public static final PCLCardData DATA = Register(Mayuri.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Electric, PCLCardTarget.Random)
            .SetSeriesFromClassPackage();

    public Mayuri()
    {
        super(DATA);

        Initialize(4, 21, 3, 20);
        SetUpgrade(2, 2, 0);
        SetAffinity_Light(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return other.rarity.equals(CardRarity.BASIC);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.LIGHTNING);
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), magicNumber);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber).AddCallback(() -> {
            for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents()) {
                if (CheckSpecialCondition(true)) {
                    PCLActions.Bottom.AddPowerEffectEnemyBonus(ElectrifiedPower.POWER_ID, secondaryValue);
                    break;
                }
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        float estimatedDifference = player.hasPower(VulnerablePower.POWER_ID) ? 1f : 1f + (PCLVulnerablePower.ATTACK_MULTIPLIER + PCLCombatStats.GetEffectBonus(VulnerablePower.POWER_ID)) / 100f;
        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents()) {
            if (intent.GetDamage(true) * estimatedDifference > player.maxHealth / 4f) {
                return true;
            }
        }
        return false;
    }
}