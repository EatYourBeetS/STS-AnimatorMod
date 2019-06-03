package eatyourbeets.actions;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

public class SummonMonsterAction extends AnimatorAction
{
    private final AbstractMonster monster;
    private final boolean isMinion;

    public SummonMonsterAction(AbstractMonster monster, boolean isMinion)
    {
        this.isMinion = isMinion;
        this.monster = monster;
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

        if (AbstractDungeon.player.hasRelic("Philosopher's Stone"))
        {
            this.monster.addPower(new StrengthPower(this.monster, 1));
            AbstractDungeon.onModifyPower();
        }
    }

    public SummonMonsterAction(AbstractMonster monster)
    {
        this(monster, true);
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            this.monster.animX = 1200.0F * Settings.scale;
            this.monster.init();
            this.monster.applyPowers();

            int index;
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
            for (index = 0; index < monsters.size(); index++)
            {
                if (this.monster.drawX > monsters.get(index).drawX)
                {
                    break;
                }
            }

            monsters.add(index, this.monster);

            if (ModHelper.isModEnabled("Lethality"))
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.monster, this.monster, new StrengthPower(this.monster, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation"))
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.monster, this.monster, new SlowPower(this.monster, 0)));
            }

            if (isMinion)
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.monster, this.monster, new MinionPower(this.monster)));
            }
        }

        this.tickDuration();
        if (this.isDone)
        {
            this.monster.animX = 0.0F;
            this.monster.showHealthBar();
            this.monster.usePreBattleAction();
        }
        else
        {
            this.monster.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.scale, this.duration);
        }
    }
}