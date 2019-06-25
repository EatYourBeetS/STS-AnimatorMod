package eatyourbeets.actions.common;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;

public class TheUnnamed_SummonDollAction extends AnimatorAction
{
    private final TheUnnamed theUnnamed;
    private AbstractMonster m;
    private int slotToFill = 0;

    public TheUnnamed_SummonDollAction(TheUnnamed theUnnamed)
    {
        this.theUnnamed = theUnnamed;
        this.actionType = ActionType.SPECIAL;
        if (Settings.FAST_MODE)
        {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }
        else
        {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }

        this.duration = this.startDuration;
        int slot = this.identifySlot(theUnnamed.minions);
        if (slot == -1)
        {
            logger.info("INCORRECTLY ATTEMPTED TO CHANNEL GREMLIN.");
        }
        else
        {
            this.slotToFill = slot;
            this.m = this.GetMinion(slot);
            theUnnamed.minions[slot] = this.m;
            if (AbstractDungeon.player.hasRelic("Philosopher's Stone"))
            {
                this.m.addPower(new StrengthPower(this.m, 1));
                AbstractDungeon.onModifyPower();
            }
        }
    }

    private int identifySlot(AbstractMonster[] minions)
    {
        for (int i = 0; i < minions.length; ++i)
        {
            if (minions[i] == null || minions[i].isDying)
            {
                return i;
            }
        }

        return -1;
    }

    private AbstractMonster GetMinion(int slot)
    {
        float x;
        float y;
        switch (slot)
        {
            case 0:
                x = -180.0F;
                y = 262.0F;
                break;
            case 1:
                x = -366.0F;
                y = 154.0F;
                break;
            case 2:
                x = -232.0F;
                y = -56.0F;
                break;
            default:
                x = -180.0F;
                y = 176.0F;
        }

        return new TheUnnamed_Doll(theUnnamed, x, y);
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            this.m.animX = 1200.0F * Settings.scale;
            this.m.init();
            this.m.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(this.slotToFill, this.m);
            if (ModHelper.isModEnabled("Lethality"))
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation"))
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
            }

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new MinionPower(this.m)));
        }

        this.tickDuration();
        if (this.isDone)
        {
            this.m.animX = 0.0F;
            this.m.showHealthBar();
            this.m.usePreBattleAction();
        }
        else
        {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.scale, this.duration);
        }
    }
}