package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class CrowleyEusford extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0);
        SetUpgrade(2, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.GainForce(1, true);

        if (CombatStats.CardsExhaustedThisTurn().size() > 0)
        {
            GameActions.Bottom.Motivate();
        }
    }
}