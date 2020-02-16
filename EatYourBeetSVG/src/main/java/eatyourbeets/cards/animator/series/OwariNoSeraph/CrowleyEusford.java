package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;

public class CrowleyEusford extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random);

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0);
        SetUpgrade(6, 0);
        SetScaling(0, 2, 1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainForce(1);

        if (PlayerStatistics.getCardsExhaustedThisTurn() > 0)
        {
            GameActions.Bottom.Motivate();
        }
    }
}