package eatyourbeets.actions._legacy.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions._legacy.common.DieAction;
import eatyourbeets.utilities.GameActionsHelper;

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
            GameActionsHelper.AddToBottom(new WaitAction(0.8f));
            GameActionsHelper.AddToBottom(new VFXAction(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2.0F));
            for (int i = 1; i <= 10; i ++)
            {
                GameActionsHelper.DamageTarget(owner, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE, true);
                GameActionsHelper.DamageTarget(owner, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE, true);
            }

            GameActionsHelper.DamageTarget(owner, target, 99999, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE);

            GameActionsHelper.AddToBottom(new DieAction(target));
        }

        this.tickDuration();
    }
}
