package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions.EYBAction;
import pinacolada.utilities.PCLActions;

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
        PCLActions.Bottom.Wait(0.8f);
        PCLActions.Bottom.VFX(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2f);

        for (int i = 1; i <= 10; i ++)
        {
            PCLActions.Bottom.DealDamage(source, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE)
            .SetVFX(true, false);

            PCLActions.Bottom.DealDamage(source, target, i * i * i, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE)
            .SetVFX(true, false);
        }

        PCLActions.Bottom.DealDamage(source, target, 99999, DamageInfo.DamageType.HP_LOSS, AttackEffect.NONE)
        .SetVFX(true, false);

        PCLActions.Bottom.Add(new DieAction(target));
    }
}
