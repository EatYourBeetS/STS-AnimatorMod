package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Fighter extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Fighter.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Fighter()
    {
        super(DATA);

        Initialize(10, 0, 3, 2);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 0, 1);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.IncreaseScaling(this, Affinity.Red, secondaryValue);
        }

        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.GainCounterAttack(secondaryValue);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        final AbstractCard last = GameUtilities.GetLastCardPlayed(true);
        return last != null && last instanceof EYBCard && last.type == CardType.ATTACK && ((EYBCard) last).attackType == EYBAttackType.Piercing;
    }
}