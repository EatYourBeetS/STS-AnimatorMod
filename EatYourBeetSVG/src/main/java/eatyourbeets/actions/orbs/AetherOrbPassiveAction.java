package eatyourbeets.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class AetherOrbPassiveAction extends EYBAction
{
    protected final Aether orb;

    public AetherOrbPassiveAction(Aether orb, int damage)
    {
        super(ActionType.DAMAGE);

        this.orb = orb;

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);

        Collections.reverse(enemies);
        for (AbstractMonster m : enemies)
        {
            int actualDamage = AbstractOrb.applyLockOn(m, amount);
            if (actualDamage > 0)
            {
                GameActions.Top.DealDamage(player, m, actualDamage, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL)
                .SetPiercing(true, true);
            }
        }
        GameActions.Top.SFX(SFX.ATTACK_WHIRLWIND);
        GameActions.Top.VFX(VFX.RazorWind(this.orb.hb, player.hb, MathUtils.random(1000.0F, 1200.0F) * Settings.scale, MathUtils.random(10.0F, 20.0F) * Settings.scale));
        GameActions.Top.VFX(VFX.Whirlwind(), 0);

        Complete();
    }
}