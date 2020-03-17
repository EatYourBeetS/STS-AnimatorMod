package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class KillCharacterAction extends EYBAction
{
    public KillCharacterAction(AbstractCreature source, AbstractCreature target)
    {
        super(ActionType.SPECIAL, Settings.ACTION_DUR_FAST);

        Initialize(source, target, 1);
    }

    @Override
    protected void FirstUpdate()
    {
        GameActions.Bottom.Wait(0.8f);
        GameActions.Bottom.VFX(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2.0F);

        for (int i = 1; i <= 10; i ++)
        {
            GameActions.Bottom.DealDamage(source, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE)
            .SetVFX(true, false);

            GameActions.Bottom.DealDamage(source, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE)
            .SetVFX(true, false);
        }

        GameActions.Bottom.DealDamage(source, target, 99999, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE)
        .SetVFX(true, false);

        GameActions.Bottom.Add(new DieAction(target));
    }
}
