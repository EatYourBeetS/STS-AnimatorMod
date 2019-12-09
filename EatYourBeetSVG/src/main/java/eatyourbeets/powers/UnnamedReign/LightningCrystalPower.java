package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class LightningCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(LightningCrystalPower.class.getSimpleName());

    public LightningCrystalPower(AbstractCreature owner, int value)
    {
        super(POWER_ID, owner, value);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");
        GameActions.Bottom.VFX(new LightningEffect(target.drawX, target.drawY));

        GameActions.Bottom.DealDamage(owner, target, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
        .SetOptions(true, true);
    }
}
