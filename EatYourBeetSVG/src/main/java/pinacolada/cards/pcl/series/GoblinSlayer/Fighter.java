package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Fighter extends PCLCard
{
    public static final PCLCardData DATA = Register(Fighter.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Fighter()
    {
        super(DATA);

        Initialize(10, 0, 3, 2);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.IncreaseScaling(this, PCLAffinity.Red, secondaryValue);
        }

        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.GainCounterAttack(secondaryValue);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        final AbstractCard last = PCLGameUtilities.GetLastCardPlayed(true);
        return last != null && last instanceof PCLCard && last.type == CardType.ATTACK && ((PCLCard) last).attackType == PCLAttackType.Piercing;
    }
}