package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged);

    private static final int STRENGTH_DOWN = 4;

    public Kuribayashi()
    {
        super(DATA);

        Initialize(6, 2, 2, 2);
        SetUpgrade(2, 0, 0, 2);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        GameActions.Bottom.StackPower(p, new ChokePower(m, secondaryValue));

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, 1));
        }
    }
}