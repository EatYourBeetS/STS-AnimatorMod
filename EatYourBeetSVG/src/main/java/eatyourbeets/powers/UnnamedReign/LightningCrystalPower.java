package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class LightningCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LightningCrystalPower.class.getSimpleName());

    public LightningCrystalPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner)
        {
            this.flash();
            AbstractCreature target = PlayerStatistics.GetRandomCharacter(true);
            GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
            GameActionsHelper.AddToBottom(new VFXAction(new LightningEffect(target.drawX, target.drawY)));
            GameActionsHelper.DamageTargetPiercing(owner, target, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
        }

        return damageAmount;
    }
}
