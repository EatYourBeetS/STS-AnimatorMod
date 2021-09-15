package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YoshinoHaruhiko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHaruhiko.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Random).SetSeriesFromClassPackage();

    public YoshinoHaruhiko()
    {
        super(DATA);

        Initialize(3, 0, 2, 1);
        SetUpgrade(2, 0, 0);
        SetAffinity_Red(2, 0, 0);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (GameUtilities.InBattle() && !GameUtilities.InStance(ForceStance.STANCE_ID))
        {
            return null;
        }

        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.InStance(ForceStance.STANCE_ID))
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.NORMAL, AttackEffects.BLUNT_HEAVY);
            }
        }

        if (!GameUtilities.InStance(ForceStance.STANCE_ID) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
            GameActions.Bottom.GainForce(1);
        }
    }
}