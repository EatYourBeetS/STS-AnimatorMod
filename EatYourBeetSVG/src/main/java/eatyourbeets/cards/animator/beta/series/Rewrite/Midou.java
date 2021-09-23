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
    public static final EYBCardData DATA = Register(Midou.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public Midou()
    {
        super(DATA);

        Initialize(2, 0, 1, 1);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected float GetInitialDamage()
    {
        float damage = super.GetInitialDamage();
        damage += player.filledOrbCount();
        return damage;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        if (info.IsSynergizing)
        {
            boolean hasOrbToEvoke = (player.filledOrbCount() > 0);

            GameActions.Bottom.EvokeOrb(1, EvokeOrb.Mode.Random);

            if (hasOrbToEvoke)
            {
                for (int i = 0; i < magicNumber; i++)
                {
                    GameActions.Bottom.MakeCardInHand(new Burn());
                }
            }
        }
    }
}