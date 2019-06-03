package eatyourbeets.actions.Orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.orbs.Air;
import eatyourbeets.powers.PlayerStatistics;

public class AirOrbPassiveAction extends AnimatorAction
{
    private final Air wind;

    public AirOrbPassiveAction(Air wind)
    {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.wind = wind;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
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