package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class CrowleyEusford extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random);

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0);
        SetUpgrade(2, 0);
        SetScaling(0, 2, 1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.GainForce(1, true);

        if (CombatStats.CardsExhaustedThisTurn() > 0)
        {
            GameActions.Bottom.Motivate();
        }
    }
}