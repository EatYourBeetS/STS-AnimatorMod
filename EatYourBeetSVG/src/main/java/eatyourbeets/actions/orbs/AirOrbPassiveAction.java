package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.orbs.Aether;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class AirOrbPassiveAction extends AnimatorAction
{
    private final Aether wind;

    public AirOrbPassiveAction(Aether wind)
    {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.wind = wind;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);
        Collections.reverse(enemies);

        for (AbstractMonster m : enemies)
        {
            int actualDamage = AbstractOrb.applyLockOn(m, wind.passiveAmount);
            if (actualDamage > 0)
            {
                GameActionsHelper.DamageTargetPiercing(p, m, actualDamage, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL);
            }
        }

        GameActionsHelper.SFX("ATTACK_WHIRLWIND");
        GameActionsHelper.VFX(new WhirlwindEffect(), 0);

        GameActionsHelper.ResetOrder();

        this.isDone = true;
    }
}