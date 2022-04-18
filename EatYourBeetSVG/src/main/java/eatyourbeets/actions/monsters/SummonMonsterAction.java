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
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class SummonMonsterAction extends EYBAction
{
    private final AbstractMonster monster;
    private final boolean isMinion;
    private float startingX = 1200f;

    public SummonMonsterAction(AbstractMonster monster)
    {
        this(monster, true);
    }

    public SummonMonsterAction(AbstractMonster monster, boolean isMinion)
    {
        super(ActionType.SPECIAL);

        this.isMinion = isMinion;
        this.monster = monster;
        this.duration = this.startDuration;

        for (AbstractRelic relic : player.relics)
        {
            relic.onSpawnMonster(monster);
        }
    }

    public SummonMonsterAction SetAnimationOffset(float x)
    {
        this.startingX = x;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        this.monster.animX = startingX * Settings.scale;
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

        if (ModHelper.isModEnabled(Lethality.ID))
        {
            GameActions.Bottom.StackPower(new StrengthPower(this.monster, 3));
        }

        if (ModHelper.isModEnabled(TimeDilation.ID))
        {
            GameActions.Bottom.StackPower(new SlowPower(this.monster, 0)).SkipIfZero(false);
        }

        if (isMinion)
        {
            GameActions.Bottom.ApplyPower(monster, monster, new MinionPower(this.monster));
        }
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
            this.monster.animX = Interpolation.fade.apply(0f, startingX * Settings.scale, this.duration);
        }
    }
}