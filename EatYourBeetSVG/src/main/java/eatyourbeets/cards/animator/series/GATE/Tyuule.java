package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Tyuule extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tyuule.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 0, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Dark(1, 1, 0);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Green, 2);

        SetFading(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            for (AbstractPower debuff : enemy.powers)
            {
                for (PowerHelper pw1 : GameUtilities.GetCommonDebuffs())
                {
                    if (pw1.ID.equals(debuff.ID))
                    {
                        GameActions.Bottom.StackPower(TargetHelper.Normal(enemy), pw1, 1);
                        break;
                    }
                }
            }
        }

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.StackPower(new NoxiousFumesPower(p, 1));
        }
    }
}