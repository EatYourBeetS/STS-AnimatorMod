package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
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
                if (PCLGameUtilities.IsCommonDebuff(debuff)) {
                    PCLActions.Bottom.StackPower(player, debuff, 1);
                }
            }
        }

        PCLActions.Bottom.ApplyPoison(p, m, GetXValue());
        TrySpendAffinity(PCLAffinity.Green, PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green, true));
    }
}