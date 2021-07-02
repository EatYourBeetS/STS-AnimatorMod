package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.animator.beta.special.JumpyDumpty;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Klee extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Klee.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    static
    {
        DATA.AddPreview(new JumpyDumpty(), false);
    }

    public Klee()
    {
        super(DATA);

        Initialize(5, 0, 2, 2);
        SetUpgrade(1, 0, 1, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.GenshinImpact);
        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m1.hb.cX, m1.hb.cY), 0.1F);
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.ApplyBurning(p, enemy, magicNumber);
        }

        int numberOfFire = JUtils.Count(player.orbs, orb -> Fire.ORB_ID.equals(orb.ID));
        int additionalCount = (numberOfFire > 0 && numberOfFire >= (p.maxOrbs / 2.0) ? 1 : 0);
        for (int i = 0; i < secondaryValue + additionalCount; i++)
        {
            GameActions.Bottom.MakeCardInDiscardPile(new JumpyDumpty());
        }
    }
}