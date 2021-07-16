package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Megumin()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade( 2, 0);

        SetAffinity_Blue(2, 0, 4);

        SetExhaust(true);
        SetUnique(true, true);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(VFX.FlameBarrierEffect(m1.hb));
            GameActions.Bottom.VFX(VFX.ExplosionSmallEffect(m1.hb));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
            .IncludeMasterDeck(true)
            .IsCancellable(false);
        }
    }
}