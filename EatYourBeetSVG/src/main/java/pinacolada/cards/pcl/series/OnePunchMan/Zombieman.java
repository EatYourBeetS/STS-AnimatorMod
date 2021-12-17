package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Zombieman extends PCLCard
{
    public static final PCLCardData DATA = Register(Zombieman.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Zombieman()
    {
        super(DATA);

        Initialize(7, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Dark(1);

        SetObtainableInCombat(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int[] damage = DamageInfo.createDamageMatrix(secondaryValue, true, true);
        PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.LoseHP(secondaryValue, AttackEffects.BLUNT_LIGHT).CanKill(false).IgnoreTempHP(true);
        PCLActions.Bottom.Flash(this);
    }

    @Override
    protected float GetInitialDamage()
    {
        int multiplier = (int)(10 * (1 - PCLGameUtilities.GetHealthPercentage(player)));
        if (player.currentHealth == 1)
        {
            multiplier += 1;
        }

        return super.GetInitialDamage() + (magicNumber * multiplier);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }
}