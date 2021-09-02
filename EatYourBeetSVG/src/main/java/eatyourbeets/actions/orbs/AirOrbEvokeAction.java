package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AirOrbEvokeAction extends EYBAction
{
    public static final int HAND_THRESHOLD = 5;
    private final int hits;

    public AirOrbEvokeAction(int damage, int hits)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        Initialize(damage + player.hand.size() / HAND_THRESHOLD);
        this.hits = hits;
    }

    @Override
    protected void FirstUpdate()
    {
        if (GameUtilities.GetEnemies(true).size() > 0) {
            SFX.Play(SFX.ANIMATOR_ORB_AIR_EVOKE);
            int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
            for (int i = 0; i < hits; i++) {
                GameActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL)
                        .SetDamageEffect((enemy, __) -> GameEffects.List.Add(VFX.Tornado(enemy.hb)))
                        .SetPiercing(true, true)
                        .SetVFX(true, false);
            }
        }
        Complete();
    }
}