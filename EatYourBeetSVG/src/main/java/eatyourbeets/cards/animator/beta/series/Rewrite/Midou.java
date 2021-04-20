package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Midou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Midou.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Midou()
    {
        super(DATA);

        Initialize(5, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.MakeCardInHand(new Burn());
        }
    }
}