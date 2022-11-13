package eatyourbeets.cards.animatorClassic.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class SuzuneAmano extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(SuzuneAmano.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(7, 0, 3, 6);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(1, 0, 0);


    }

    @Override
    protected float GetInitialDamage()
    {
        float damage = super.GetInitialDamage();
        if (IsStarter())
        {
            damage += (JUtils.Count(player.orbs, o -> Fire.ORB_ID.equals(o.ID)) * secondaryValue);
        }

        return damage;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);

        if (IsStarter())
        {
            GameActions.Bottom.Draw(JUtils.Count(player.orbs, o -> Fire.ORB_ID.equals(o.ID)));
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, !upgraded)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            if (cards != null && cards.size() > 0)
            {
                GameActions.Bottom.ApplyBurning(player, enemy, magicNumber);
            }
        });
    }
}