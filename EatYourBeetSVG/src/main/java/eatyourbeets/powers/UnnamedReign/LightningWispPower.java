package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class LightningWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LightningWispPower.class.getSimpleName());

    public LightningWispPower(AbstractCreature owner, int value)
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
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
        {
            GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
            GameActionsHelper.AddToBottom(new VFXAction(new LightningEffect(c.drawX, c.drawY)));
            GameActionsHelper.DamageTargetPiercing(owner, c, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
        }
    }
}