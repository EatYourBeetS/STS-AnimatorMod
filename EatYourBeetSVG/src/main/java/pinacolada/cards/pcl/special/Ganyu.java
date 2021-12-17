package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Status_Frostbite;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Ganyu extends PCLCard
{
    public static final PCLCardData DATA = Register(Ganyu.class).SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Ranged).SetColor(CardColor.COLORLESS)
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
            PCLActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true))
                {
                    if (PCLGameUtilities.GetPowerAmount(m,LockOnPower.POWER_ID) >= 1)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return PCLGameUtilities.GetPowerAmount(m,LockOnPower.POWER_ID) >= 1;
            }
        }

        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, GR.Enums.AttackEffect.CLAW);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber);
        for (int i = 0; i < secondaryValue; i++) {
            PCLActions.Bottom.MakeCardInDrawPile(new Status_Frostbite())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}