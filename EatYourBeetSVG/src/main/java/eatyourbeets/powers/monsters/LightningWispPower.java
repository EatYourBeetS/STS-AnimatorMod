package eatyourbeets.powers.monsters;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LightningWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LightningWispPower.class);

    public LightningWispPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
        {
            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE);
            GameActions.Bottom.VFX(VFX.Lightning(c.hb));
            GameActions.Bottom.DealDamage(owner, c, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetPiercing(true, true);
        }
    }
}