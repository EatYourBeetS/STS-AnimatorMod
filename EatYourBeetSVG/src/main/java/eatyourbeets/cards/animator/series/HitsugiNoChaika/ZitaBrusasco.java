package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class ZitaBrusasco extends AnimatorCard {
    public static final EYBCardData DATA = Register(ZitaBrusasco.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public ZitaBrusasco() {
        super(DATA);

        Initialize(9, 0, 0);
        SetUpgrade(4, 0, 0);

        SetAffinity_Fire();
        SetAffinity_Steel();
        SetAffinity_Thunder();
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        boolean canPlay = false;

        if (player == null)
        {
            return;
        }

        if (player.orbs == null)
        {
            return;
        }

        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && orb.ID != null && !orb.ID.equals(EmptyOrbSlot.ORB_ID))
            {
                canPlay = true;
                break;
            }
        }

        SetUnplayable(!canPlay);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SMALL_EXPLOSION);
        GameActions.Bottom.EvokeOrb(1);
    }
}