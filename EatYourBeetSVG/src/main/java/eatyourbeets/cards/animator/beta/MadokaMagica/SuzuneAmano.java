package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class SuzuneAmano extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(SuzuneAmano.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(8, 0, 3, 3);
        SetUpgrade(0,0,0,0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (GetFireOrbCount() * secondaryValue);
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

    private int GetFireOrbCount()
    {
        int numFire = 0;

        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && Fire.ORB_ID.equals(orb.ID))
            {
                numFire++;
            }
        }

        return numFire;
    }
}