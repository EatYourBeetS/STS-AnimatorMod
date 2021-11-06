package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class SuzuneAmano extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuzuneAmano.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(7, 0, 3, 6);
        SetUpgrade(0, 0, 0, 0);


        SetSynergy(Synergies.MadokaMagica);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);

        if (IsStarter())
        {
            GameActions.Bottom.Draw(JUtils.Count(player.orbs, o -> Fire.ORB_ID.equals(o.ID)));
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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