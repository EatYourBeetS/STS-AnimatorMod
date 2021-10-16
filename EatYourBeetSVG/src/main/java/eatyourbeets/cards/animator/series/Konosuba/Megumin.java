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
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    public static final int ATTACK_TURNS = 2;
    public static final int SYNERGY_REQUIREMENT = 5;

    public Megumin()
    {
        super(DATA);

        Initialize(10, 0, 5, ATTACK_TURNS);
        SetUpgrade( 2, 0);

        SetAffinity_Fire(2);
        SetAffinity_Mind();

        SetExhaust(true);
        SetUnique(true, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.9f, 1.1f);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.8f, 1.2f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.7f, 1.3f);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f, 1.5f);

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new FlameBarrierEffect(m1.hb_x, m1.hb_y));
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                    .IncludeMasterDeck(true)
                    .IsCancellable(false);
        }
    }
}