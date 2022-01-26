package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Alexander extends PCLCard
{
    public static final PCLCardData DATA = Register(Alexander.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();

    public Alexander()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Orange(1);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.PlayCopy(this, null);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);
        if (CheckPrimaryCondition(true))
        {
            PCLActions.Bottom.Draw(1);
        }
        else {
            PCLActions.Bottom.DrawNextTurn(1);
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse){
        return PCLGameUtilities.IsAffinityHighest(PCLAffinity.Red);
    }
}