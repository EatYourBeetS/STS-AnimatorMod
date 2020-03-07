package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class InverseTohka extends AnimatorCard {
    public static final EYBCardData DATA = Register(InverseTohka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    public InverseTohka() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.eatyourbeets.cards.animator.beta.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
    }
}