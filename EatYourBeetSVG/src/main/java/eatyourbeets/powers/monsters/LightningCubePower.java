package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LightningCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LightningCubePower.class);

    public LightningCubePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.priority = -99;

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        final AbstractCreature target = owner.isPlayer ? GameUtilities.GetRandomEnemy(true) : player;
        GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE);
        GameActions.Bottom.VFX(VFX.Lightning(target.hb));
        GameActions.Bottom.DealDamage(owner, target, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
        .SetPiercing(true, true);
    }
}
