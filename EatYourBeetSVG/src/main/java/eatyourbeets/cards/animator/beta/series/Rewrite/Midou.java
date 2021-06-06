package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Midou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Midou.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Midou()
    {
        super(DATA);

        Initialize(2, 0, 1, 1);
        SetUpgrade(3, 0, 0, 0);
        SetSynergy(Synergies.Rewrite);
    }

    @Override
    protected float GetInitialDamage()
    {
        float damage = super.GetInitialDamage();
        damage += player.filledOrbCount();
        return damage;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        if (isSynergizing)
        {
            GameActions.Bottom.EvokeOrb(1, EvokeOrb.Mode.Random);

            for (int i=0; i<magicNumber; i++)
            {
                GameActions.Bottom.MakeCardInHand(new Burn());
            }
        }
    }
}