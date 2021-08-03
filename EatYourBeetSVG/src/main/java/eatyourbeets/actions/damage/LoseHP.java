package eatyourbeets.actions.damage;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class LoseHP extends EYBAction
{
    protected boolean ignoreTempHP;

    public LoseHP(AbstractCreature target, AbstractCreature source, int amount)
    {
        this(target, source, amount, AttackEffect.NONE);
    }

    public LoseHP(AbstractCreature target, AbstractCreature source, int amount, AttackEffect effect)
    {
        super(ActionType.DAMAGE, 0.33f);

        this.attackEffect = effect;

        Initialize(source, target, amount);
    }

    public LoseHP IgnoreTempHP(boolean ignoreTempHP)
    {
        this.ignoreTempHP = ignoreTempHP;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.target.currentHealth > 0)
        {
            GameEffects.List.Attack(target, attackEffect, 0, 0);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            int tempHP = 0;
            if (ignoreTempHP)
            {
                tempHP = TempHPField.tempHp.get(target);
                TempHPField.tempHp.set(target, 0);
            }

            this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS));

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameUtilities.ClearPostCombatActions();
            }
            else if (tempHP > 0)
            {
                TempHPField.tempHp.set(target, tempHP);
            }

            if (!Settings.FAST_MODE)
            {
                GameActions.Top.Wait(0.1f);
            }
        }
    }
}
