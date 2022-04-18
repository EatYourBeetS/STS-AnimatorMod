package eatyourbeets.actions.monsters;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Lethality;
import com.megacrit.cardcrawl.daily.mods.TimeDilation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class TheUnnamed_SummonDollAction extends EYBAction
{
    private final TheUnnamed theUnnamed;
    private AbstractMonster monster;
    private int slotToFill = 0;

    public TheUnnamed_SummonDollAction(TheUnnamed theUnnamed)
    {
        super(ActionType.SPECIAL, Settings.FAST_MODE ? Settings.ACTION_DUR_FAST : Settings.ACTION_DUR_LONG);

        this.theUnnamed = theUnnamed;

        int slot = IdentifySlot(theUnnamed.minions);
        if (slot == -1)
        {
            JUtils.LogInfo(this, "INCORRECTLY ATTEMPTED TO CHANNEL GREMLIN.");
        }
        else
        {
            this.slotToFill = slot;
            this.monster = GetMinion(slot);
            theUnnamed.minions[slot] = this.monster;

            for (AbstractRelic relic : player.relics)
            {
                relic.onSpawnMonster(monster);
            }
        }
    }

    private int IdentifySlot(AbstractMonster[] minions)
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
                x = -180f;
                y = 262f;
                break;
            case 1:
                x = -366f;
                y = 154f;
                break;
            case 2:
                x = -232f;
                y = -56f;
                break;
            default:
                x = -180f;
                y = 176f;
        }

        return new TheUnnamed_Doll(theUnnamed, x, y);
    }

    @Override
    protected void FirstUpdate()
    {
        monster.animX = 1200f * Settings.scale;
        monster.init();
        monster.applyPowers();

        AbstractDungeon.getMonsters().addMonster(slotToFill, monster);

        if (ModHelper.isModEnabled(Lethality.ID))
        {
            GameActions.Bottom.StackPower(new StrengthPower(monster, 3));
        }

        if (ModHelper.isModEnabled(TimeDilation.ID))
        {
            GameActions.Bottom.StackPower(new SlowPower(monster, 0)).SkipIfZero(false);
        }

        GameActions.Bottom.ApplyPower(monster, monster, new MinionPower(monster));
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            this.monster.animX = 0f;
            this.monster.showHealthBar();
            this.monster.usePreBattleAction();
        }
        else
        {
            this.monster.animX = Interpolation.fade.apply(0f, 1200f * Settings.scale, this.duration);
        }
    }
}