package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Elsword extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Elsword.class).SetAttack(2, CardRarity.COMMON);

    public Elsword()
    {
        super(DATA);

        Initialize(14, 0, 1, 4);
        SetUpgrade(2,  0, 0, 1);
        SetScaling(0, 0, 2);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ChannelOrb(new Fire(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.ApplyBurning(p, m, secondaryValue);
        GameActions.Bottom.Cycle(name, magicNumber);
    }
}