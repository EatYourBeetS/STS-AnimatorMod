package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Raven extends PCLCard
{
    public static final PCLCardData DATA = Register(Raven.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Raven()
    {
        super(DATA);

        Initialize(3, 0, 1, 2);
        SetUpgrade(3, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (PCLGameUtilities.IsAttacking(m.intent))
        {
            PCLActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
        else
        {
            PCLActions.Bottom.ApplyVulnerable(p, m, secondaryValue);
        }

        PCLActions.Bottom.DrawNextTurn(1);
    }
}