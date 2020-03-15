package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class ZadkielAction extends EYBAction {
    public ZadkielAction() {
        super(ActionType.DAMAGE);
    }

    @Override
    protected void FirstUpdate()
    {
        int[] damageMatrix = DamageInfo.createDamageMatrix(player.currentBlock);

        GameActions.Bottom.VFX(new WhirlwindEffect());
        GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
    }
}
