package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tyuule extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tyuule.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 2, 0, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Dark(1, 1, 0);
        SetAffinity_Green(1);
    }

    @Override
    public int GetXValue() {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Green, true) + secondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            for (AbstractPower debuff : enemy.powers)
            {
                if (WeakPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyWeak(player, enemy, 1);
                }
                else if (VulnerablePower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyVulnerable(player, enemy, 1);
                }
                else if (PoisonPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyPoison(player, enemy, 1);
                }
                else if (BurningPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyBurning(player, enemy, 1);
                }
                else if (GainStrengthPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ReduceStrength(enemy, 1, true);
                }
            }
        }

        GameActions.Bottom.ApplyPoison(p, m, GetXValue());
        TrySpendAffinity(Affinity.Green, CombatStats.Affinities.GetAffinityLevel(Affinity.Green, true));
    }
}