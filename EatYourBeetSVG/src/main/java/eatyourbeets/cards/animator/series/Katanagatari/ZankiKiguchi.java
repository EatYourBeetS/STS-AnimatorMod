package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
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

        Initialize(2, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(0, 0, 1);

        SetAffinityRequirement(Affinity.Star, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.GainTemporaryStats(magicNumber, 0, 0);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
            .AddCallback(stance ->
            {
                if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID))
                {
                    GameActions.Bottom.SFX(SFX.DAMARU, 0.8f, 0.95f, 0.5f);
                    GameActions.Bottom.GainEnergy(1);
                    GameActions.Bottom.Draw(2);
                }
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return !GameUtilities.InStance(NeutralStance.STANCE_ID) && super.CheckSpecialCondition(tryUse);
    }
}