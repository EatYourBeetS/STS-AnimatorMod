package eatyourbeets.cards.animator.series.OwariNoSeraph;

import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CrowleyEusford extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0, 3);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(2, 0, 6);
        SetAffinity_Green(1, 0, 6);
        SetAffinity_Dark(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCardHeal(this);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = GameUtilities.GetHealthRecoverAmount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY)
        .AddCallback(() ->
        {
            GameActions.Bottom.GainAgility(1, true);
            GameActions.Bottom.GainForce(1, true);
        });
        GameActions.Bottom.RecoverHP(magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Motivate(1);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.CardsExhaustedThisTurn().size() > 0;
    }
}