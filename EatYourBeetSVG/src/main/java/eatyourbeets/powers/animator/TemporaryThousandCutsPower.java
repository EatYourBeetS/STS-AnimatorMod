package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryThousandCutsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TemporaryThousandCutsPower.class);

    public TemporaryThousandCutsPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        int[] damageMatrix = DamageInfo.createDamageMatrix(this.amount, true);
        GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_DIAGONAL);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }

}
