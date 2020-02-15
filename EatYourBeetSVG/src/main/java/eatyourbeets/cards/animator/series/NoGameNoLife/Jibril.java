package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.actions.orbs.ShuffleOrbs;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;

public class Jibril extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Jibril.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Jibril()
    {
        super(DATA);

        Initialize(8, 0, 2);
        SetUpgrade(2, 0, 0);
        SetScaling(3, 0, 0);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Dark(), true);
        GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.VIOLET, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.3F);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        if (HasSynergy())
        {
            GameActions.Bottom.Add(new ShuffleOrbs(1));
            GameActions.Bottom.Add(new TriggerOrbPassiveAbility(magicNumber, false, true));
        }

        if (upgraded)
        {
            IntellectPower.PreserveOnce();
        }
    }
}