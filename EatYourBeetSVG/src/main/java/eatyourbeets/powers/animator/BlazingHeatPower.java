package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class BlazingHeatPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BlazingHeatPower.class);

    public BlazingHeatPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Fire.ORB_ID.equals(orb.ID)) {
            if (owner.isPlayer)
            {
                int[] damageMatrix = DamageInfo.createDamageMatrix(orb.passiveAmount * this.amount, true);
                GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
            }
            else {
                GameActions.Bottom.DealDamage(null, player, orb.passiveAmount * this.amount, DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
            }
        }
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        for (AbstractMonster m : GameUtilities.GetEnemies(true)) {
            BurningPower bp = GameUtilities.GetPower(m, BurningPower.class);
            FreezingPower fp = GameUtilities.GetPower(m, FreezingPower.class);
            if (bp != null) {
                GameActions.Bottom.LoseHP(source, m, bp.GetPassiveDamage(), AttackEffects.FIRE)
                        .CanKill(owner == null || !owner.isPlayer);
            }
            if (fp != null) {
                GameActions.Bottom.LoseHP(source, m, fp.GetPassiveDamage(), AttackEffects.FIRE)
                        .CanKill(owner == null || !owner.isPlayer);
            }
        }
    }
}
