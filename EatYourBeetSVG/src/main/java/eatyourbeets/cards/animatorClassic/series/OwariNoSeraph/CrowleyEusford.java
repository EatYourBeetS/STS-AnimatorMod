package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class CrowleyEusford extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random);

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0);
        SetUpgrade(2, 0);
        SetScaling(0, 2, 1);

        
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
            GameActions.Bottom.Motivate(1);
        }
    }
}