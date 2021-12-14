package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.animator.beta.status.Status_Frostbite;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Ganyu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ganyu.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> data.AddPreview(new Status_Frostbite(), false));

    public Ganyu()
    {
        super(DATA);

        Initialize(33, 0, 2, 5);
        SetUpgrade(9, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    if (GameUtilities.GetPowerAmount(m,LockOnPower.POWER_ID) >= 1)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return GameUtilities.GetPowerAmount(m,LockOnPower.POWER_ID) >= 1;
            }
        }

        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, GR.Enums.AttackEffect.CLAW);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber);
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.MakeCardInDrawPile(new Status_Frostbite())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}