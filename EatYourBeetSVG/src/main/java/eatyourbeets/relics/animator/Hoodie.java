package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnModifyDamageFirstSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;

public class Hoodie extends AnimatorRelic implements OnModifyDamageFirstSubscriber
{
    public static final String ID = CreateFullID(Hoodie.class);

    public Hoodie()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        CombatStats.onModifyDamageFirst.ToggleSubscription(this, enabled);
    }

    @Override
    public int OnModifyDamageFirst(AbstractCreature target, DamageInfo info, int damage)
    {
        if (target == player && GameUtilities.IsPlayerTurn(false) && (info.type != DamageInfo.DamageType.NORMAL || info.owner == null || info.owner.isPlayer))
        {
            flash();
            return damage / 2;
        }

        return damage;
    }
}