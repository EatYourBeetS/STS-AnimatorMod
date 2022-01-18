package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.LogHorizon.Soujiro;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Soujiro_Kawara extends PCLCard
{
    public static final PCLCardData DATA = Register(Soujiro_Kawara.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Normal)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Kawara()
    {
        super(DATA);

        Initialize(5, 2, 3);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (enemy != null && PCLGameUtilities.IsAttacking(enemy.intent))
        {
            return super.ModifyBlock(enemy, amount + magicNumber);
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }
}