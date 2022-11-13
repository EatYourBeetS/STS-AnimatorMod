package eatyourbeets.cards.animatorClassic.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.actions.orbs.ShuffleOrbs;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Jibril extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Jibril.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Jibril()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(4, 0, 0);
        SetScaling(3, 0, 0);

        SetEvokeOrbCount(1);
        
        SetSpellcaster();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.VIOLET.cpy(), ShockWaveEffect.ShockWaveType.ADDITIVE), 0.3f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.ChannelOrb(new Dark());

        if (info.IsSynergizing)
        {
            GameActions.Bottom.Add(new ShuffleOrbs(1));
            GameActions.Bottom.Add(new TriggerOrbPassiveAbility(magicNumber, false, true, null));
        }

        GameUtilities.RetainPower(Affinity.Blue);
    }
}
