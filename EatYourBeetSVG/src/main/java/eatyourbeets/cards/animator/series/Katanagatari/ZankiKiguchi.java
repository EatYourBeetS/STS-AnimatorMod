package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ZankiKiguchi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ZankiKiguchi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public ZankiKiguchi()
    {
        super(DATA);

        Initialize(2, 0, 7, 1);
        SetUpgrade(1, 0, 3);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (CheckPrimaryCondition(true))
        {
            GameActions.Bottom.GainVelocity(secondaryValue);
        }

        if (info.CanActivateSemiLimited)
        {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
            .AddCallback(info, (info2, stance) ->
            {
                if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && info2.TryActivateSemiLimited())
                {
                    GameActions.Bottom.GainInspiration(1);
                }
            });
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return GameUtilities.GetPowerAmount(Affinity.Green) == 0;
    }
}