package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Spearman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Spearman.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing);
    static
    {
        DATA.AddPreview(new FakeAbstractCard(new Wound()), false);
    }

    public Spearman()
    {
        super(DATA);

        Initialize(9, 0, 1);
        SetUpgrade(4, 0);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.GainAgility(magicNumber, true);
        GameActions.Bottom.MakeCardInHand(new Wound());

        if (HasSynergy())
        {
            GameActions.Bottom.GainForce(1);
        }
    }
}