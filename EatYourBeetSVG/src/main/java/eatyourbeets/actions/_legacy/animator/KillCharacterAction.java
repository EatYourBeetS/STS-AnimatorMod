package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions._legacy.common.DieAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class KillCharacterAction extends AbstractGameAction
{
    private final AbstractCreature owner;

    public KillCharacterAction(AbstractCreature owner, AbstractCreature target)
    {
        this.owner = owner;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            GameActions.Bottom.Wait(0.8f);
            GameActions.Bottom.VFX(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2.0F);
            for (int i = 1; i <= 10; i ++)
            {
                GameActionsHelper_Legacy.DamageTarget(owner, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE, true);
                GameActionsHelper_Legacy.DamageTarget(owner, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE, true);
            }

            GameActionsHelper_Legacy.DamageTarget(owner, target, 99999, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE);

            GameActionsHelper_Legacy.AddToBottom(new DieAction(target));
        }

        this.tickDuration();
    }
}
