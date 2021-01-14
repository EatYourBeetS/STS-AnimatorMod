package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Kawara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Kawara.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Normal);
    static
    {
        DATA.AddPreview(new Soujiro(), false);
    }

    public Soujiro_Kawara()
    {
        super(DATA);

        Initialize(5, 0, 0);
        SetUpgrade(2, 0, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}