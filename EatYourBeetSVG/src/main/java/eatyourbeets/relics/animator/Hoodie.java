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
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        CombatStats.onModifyDamageFirst.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        CombatStats.onModifyDamageFirst.Unsubscribe(this);
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