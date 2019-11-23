package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameUtilities;

public class LightningCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LightningCubePower.class.getSimpleName());

    public LightningCubePower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;
        this.priority = -99;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        AbstractCreature target;

        if (owner.isPlayer)
        {
            target = GameUtilities.GetRandomEnemy(true);
        }
        else
        {
            target = AbstractDungeon.player;
        }

        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
        GameActionsHelper.AddToBottom(new VFXAction(new LightningEffect(target.drawX, target.drawY)));
        GameActionsHelper.DamageTargetPiercing(owner, target, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }
}
