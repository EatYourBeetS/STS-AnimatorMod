package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HidekiHinata extends PCLCard
{
    public static final PCLCardData DATA = Register(HidekiHinata.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public HidekiHinata()
    {
        super(DATA);

        Initialize(6, 0, 2, 6);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(0,0,1);

        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && PCLGameUtilities.IsAttacking(enemy.intent))
        {
            return super.ModifyDamage(enemy, amount + secondaryValue);
        }
        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);

        if (!PCLGameUtilities.IsAttacking(m.intent)) {
            PCLActions.Bottom.GainSupportDamage(magicNumber);
        }
    }

    @Override
    public void triggerOnAfterlife() {
        PCLActions.Bottom.GainEnergyNextTurn(1);
    }
}