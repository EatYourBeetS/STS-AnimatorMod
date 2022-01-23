package pinacolada.actions.basic;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.utilities.PCLGameEffects;

public class HealCreature extends EYBActionWithCallback<Integer>
{
    protected final int playerHpLastTurn;
    protected boolean showEffect = true;
    protected boolean recover = false;

    public HealCreature(AbstractCreature source, AbstractCreature target, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FAST);

        this.playerHpLastTurn = GameActionManager.playerHpLastTurn;
        this.canCancel = false;

        Initialize(target, source, amount);

        if (amount <= 0)
        {
            Complete(0);
        }
    }

    public HealCreature Recover(boolean recover)
    {
        this.recover = recover;

        return this;
    }

    public HealCreature ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (target.isPlayer)
        {
            if (recover)
            {
                amount = Math.min(amount, playerHpLastTurn - player.currentHealth);

                if (amount <= 0)
                {
                    Complete(0);
                    return;
                }
            }
        }

        target.heal(amount, false);

        if (showEffect)
        {
            if (target.isPlayer)
            {
                AbstractDungeon.topPanel.panelHealEffect();
            }

            PCLGameEffects.Queue.Add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, amount));
        }

        Complete(amount);
    }
}