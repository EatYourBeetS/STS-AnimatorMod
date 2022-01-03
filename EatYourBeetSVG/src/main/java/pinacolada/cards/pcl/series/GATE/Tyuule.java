package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Tyuule extends PCLCard
{
    public static final PCLCardData DATA = Register(Tyuule.class)
            .SetSkill(1, CardRarity.COMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 1, 0, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Green(1);
    }

    @Override
    public int GetXValue() {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green, true) + secondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true))
        {
            for (AbstractPower debuff : enemy.powers)
            {
                if (WeakPower.POWER_ID.equals(debuff.ID))
                {
                    PCLActions.Bottom.ApplyWeak(player, enemy, 1);
                }
                else if (VulnerablePower.POWER_ID.equals(debuff.ID))
                {
                    PCLActions.Bottom.ApplyVulnerable(player, enemy, 1);
                }
                else if (PoisonPower.POWER_ID.equals(debuff.ID))
                {
                    PCLActions.Bottom.ApplyPoison(player, enemy, 1);
                }
                else if (BurningPower.POWER_ID.equals(debuff.ID))
                {
                    PCLActions.Bottom.ApplyBurning(player, enemy, 1);
                }
                else if (GainStrengthPower.POWER_ID.equals(debuff.ID))
                {
                    PCLActions.Bottom.ReduceStrength(enemy, 1, true);
                }
            }
        }

        PCLActions.Bottom.ApplyPoison(p, m, GetXValue());
        TrySpendAffinity(PCLAffinity.Green, PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green, true));
    }
}