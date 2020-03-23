package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Megumin()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade( 2, 0);
        SetScaling(4, 0, 0);

        SetExhaust(true);
        SetUnique(true, true);
        SetSynergy(Synergies.Konosuba);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 2 == 0)
        {
            upgradeDamage(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.1f);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.3f);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f);

        for (AbstractCreature m1 : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.VFX(new FlameBarrierEffect(m1.hb_x, m1.hb_y));
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);

        if (HasSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            for (AbstractCard c : GameUtilities.GetAllInstances(this))
            {
                c.upgrade();
            }
        }
    }
}