package eatyourbeets.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class AetherOrbPassiveAction extends EYBAction
{
    public AetherOrbPassiveAction(int damage)
    {
        super(ActionType.DAMAGE);

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

        GameActions.Top.SFX("ATTACK_WHIRLWIND");
        GameActions.Top.VFX(VFX.RazorWind(player.hb, MathUtils.random(2000.0F, 2200.0F) * Settings.scale, 0));
        GameActions.Top.VFX(new WhirlwindEffect(), 0);

        Complete();
    }
}