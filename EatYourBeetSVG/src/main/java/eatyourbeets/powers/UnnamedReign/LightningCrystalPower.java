package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActionsHelper;

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
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
        GameActionsHelper.AddToBottom(new VFXAction(new LightningEffect(target.drawX, target.drawY)));
        GameActionsHelper.DamageTargetPiercing(owner, target, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }
}
