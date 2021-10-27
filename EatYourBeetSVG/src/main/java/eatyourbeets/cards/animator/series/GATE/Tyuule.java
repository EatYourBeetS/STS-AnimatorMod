package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Tyuule extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tyuule.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 3, 1);

        SetAffinity_Poison();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), magicNumber);

        GameActions.Last.Callback(power -> {
            for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
            {
                for (AbstractPower debuff : enemy.powers)
                {
                    if (GameUtilities.IsCommonDebuff(debuff))
                    {
                        debuff.amount += secondaryValue;
                    }
                }
            }
        });
    }
}