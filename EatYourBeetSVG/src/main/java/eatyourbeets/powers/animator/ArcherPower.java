package eatyourbeets.powers.animator;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ArcherPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ArcherPower.class);

    public ArcherPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (!isPlayer)
        {
            return;
        }

        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            final int debuffs = GameUtilities.GetDebuffsCount(m.powers);
            for (int i = 0; i < debuffs; i++)
            {
                GameActions.Bottom.VFX(VFX.ThrowDagger(m.hb, 0.2f));
                GameActions.Bottom.DealDamage(owner, m, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                .SetVFX(true, true);
            }
        }

        this.flash();
    }
}
