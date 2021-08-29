package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.common.CounterAttackPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Sebas extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sebas.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Sebas()
    {
        super(DATA);

        Initialize(0, 9, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        final int counter = JUtils.Count(GameUtilities.GetIntents(), EnemyIntent::IsAttacking);
        if (counter > 0)
        {
            GameActions.Bottom.GainForce(counter);
            GameActions.Bottom.StackPower(new CounterAttackPower(p, counter * magicNumber));
        }

        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.GainEnergy(1);
        }
        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }
}