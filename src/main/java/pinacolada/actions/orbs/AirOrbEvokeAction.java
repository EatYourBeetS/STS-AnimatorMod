package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class AirOrbEvokeAction extends EYBAction
{
    private final int hits;

    public AirOrbEvokeAction(int damage, int hits)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        Initialize(damage);
        this.hits = hits;
    }

    @Override
    protected void FirstUpdate()
    {
        if (PCLGameUtilities.GetEnemies(true).size() > 0) {
            SFX.Play(SFX.PCL_ORB_AIR_EVOKE);
            int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
            for (int i = 0; i < hits; i++) {
                PCLActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL)
                        .SetDamageEffect((enemy, __) -> PCLGameEffects.List.Add(VFX.Tornado(enemy.hb)))
                        .SetPiercing(true, true)
                        .SetVFX(true, false);
            }
        }
        Complete();
    }
}