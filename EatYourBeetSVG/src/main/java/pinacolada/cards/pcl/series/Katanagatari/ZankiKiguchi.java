package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class ZankiKiguchi extends PCLCard
{
    public static final PCLCardData DATA = Register(ZankiKiguchi.class)
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
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.GainVelocity(secondaryValue);
        }

        if (info.CanActivateSemiLimited)
        {
            PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
            .AddCallback(info, (info2, stance) ->
            {
                if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && info2.TryActivateSemiLimited())
                {
                    PCLActions.Bottom.Draw(1);
                }
            });
        }
    }
}