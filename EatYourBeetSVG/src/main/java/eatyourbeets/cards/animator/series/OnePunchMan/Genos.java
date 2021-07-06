package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;

public class Genos extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Genos.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);

    public Genos()
    {
        super(DATA);

        Initialize(14, 0, 3, 4);
        SetUpgrade(4, 0, 0, 0);

        SetSynergy(Synergies.OnePunchMan);
        SetAffinity(1, 0, 0, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));

        if (isSynergizing)
        {
            GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        }
    }
}