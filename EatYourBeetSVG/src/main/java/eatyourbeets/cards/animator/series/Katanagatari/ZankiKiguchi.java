package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
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

        Initialize(3, 0, 2);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainInspiration(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
        .AddCallback(info, (info2, stance) ->
        {
            if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID))
            {
                GameActions.Bottom.SFX(SFX.DAMARU, 0.8f, 0.95f, 0.5f);
                GameActions.Bottom.GainForce(1, true);
                GameActions.Bottom.GainAgility(1, true);
                GameActions.Bottom.Draw(magicNumber);
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return !GameUtilities.InStance(NeutralStance.STANCE_ID);
    }
}