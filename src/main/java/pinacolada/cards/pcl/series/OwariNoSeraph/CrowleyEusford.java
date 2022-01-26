package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class CrowleyEusford extends PCLCard
{
    public static final PCLCardData DATA = Register(CrowleyEusford.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Normal, PCLCardTarget.Random)
            .SetSeriesFromClassPackage();

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0, 2);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.CanActivateSemiLimited(cardID)) {
            for (AbstractMonster mo: PCLGameUtilities.GetEnemies(true)) {
                if (PCLGameUtilities.GetPowerAmount(mo, VulnerablePower.POWER_ID) > 0) {
                    CombatStats.TryActivateSemiLimited(cardID);
                    PCLActions.Bottom.StackPower(new VigorPower(player, magicNumber));
                    PCLActions.Bottom.Flash(this);
                    break;
                }
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY);

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.Motivate();
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.CardsExhaustedThisTurn().size() > 0;
    }
}