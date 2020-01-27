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
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Megumin.class, EYBCardBadge.Synergy);

    public Megumin()
    {
        super(ID, 2, CardRarity.UNCOMMON, AttackType.Elemental, true);

        Initialize(12, 0);
        SetUpgrade( 2, 0);

        SetUnique(true, true);
        SetExhaust(true);

        SetSynergy(Synergies.Konosuba);
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
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (Spellcaster.GetScaling() * 4));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.1F);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2F);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.3F);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f);

        for (AbstractCreature m1 : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.VFX(new FlameBarrierEffect(m1.hb_x, m1.hb_y));
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m1.hb_x, m1.hb_y));
        }

        DealDamageToALL(AbstractGameAction.AttackEffect.NONE);

        if (HasSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            for (AbstractCard c : GameUtilities.GetAllInstances(this))
            {
                c.upgrade();
            }
        }
    }
}