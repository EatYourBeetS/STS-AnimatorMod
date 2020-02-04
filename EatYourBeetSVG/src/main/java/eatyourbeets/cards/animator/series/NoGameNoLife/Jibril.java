package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class Jibril extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register_Old(Jibril.class);

    public Jibril()
    {
        super(ID, 2, CardRarity.COMMON, EYBAttackType.Elemental, true);

        Initialize(8, 0);
        SetUpgrade(4, 0);
        SetScaling(2, 0, 0);

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
            GameActions.Bottom.GainIntellect(1);
        }
    }
}