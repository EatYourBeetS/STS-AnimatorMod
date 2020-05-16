package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class SuzuneAmano extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuzuneAmano.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(8, 0, 3, 3);
        SetUpgrade(0, 0, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (JavaUtilities.Count(player.orbs, orb -> Fire.ORB_ID.equals(orb.ID)) * secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ExhaustFromHand(name, 1, !upgraded)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            if (cards != null && cards.size() > 0)
            {
                GameActions.Bottom.ApplyBurning(player, (AbstractMonster) enemy, magicNumber);
            }
        });
    }
}