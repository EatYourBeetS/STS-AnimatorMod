package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;

public class Dust extends PCLCard
{
    public static final PCLCardData DATA = Register(Dust.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Dust()
    {
        super(DATA);

        Initialize(7, 0, 2, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Orange(0,0,1);

        SetAffinityRequirement(PCLAffinity.Red, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        PCLActions.Bottom.Draw(1).SetFilter(c -> c.rarity.equals(CardRarity.BASIC) || c.rarity.equals(CardRarity.COMMON), false);

        if (IsStarter() && TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Normal(m), magicNumber);
        }
    }
}