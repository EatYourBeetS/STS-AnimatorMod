package eatyourbeets.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class DealDamageToAll extends EYBActionWithCallback<ArrayList<AbstractCreature>>
{
    public int[] damage;

    protected final ArrayList<AbstractCreature> targets = new ArrayList<>();
    protected BiConsumer<AbstractCreature, Boolean> onDamageEffect;
    protected boolean bypassBlock;
    protected boolean bypassThorns;

    public DealDamageToAll(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect)
    {
        this(source, amount, type, effect, false);
    }

    public DealDamageToAll(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect, boolean isFast)
    {
        super(ActionType.DAMAGE, isFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.damage = amount;

        Initialize(source, null, amount[0]);
    }

    public DealDamageToAll SetDamageEffect(BiConsumer<AbstractCreature, Boolean> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamageToAll SetOptions(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        boolean mute = false;

        int i = 0;
        for (AbstractMonster enemy : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            if (!GameUtilities.IsDeadOrEscaped(enemy))
            {
                if (onDamageEffect != null)
                {
                    onDamageEffect.accept(enemy, !mute);
                }
                else
                {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(enemy.hb.cX, enemy.hb.cY, this.attackEffect, mute));
                }

                mute = true;
            }

            i += 1;
        }
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (this.isDone)
        {
            for (AbstractPower p : AbstractDungeon.player.powers)
            {
                p.onDamageAllEnemies(this.damage);
            }

            int i = 0;
            for (AbstractMonster enemy : AbstractDungeon.getCurrRoom().monsters.monsters)
            {
                if (!GameUtilities.IsDeadOrEscaped(enemy))
                {
                    if (this.attackEffect == AttackEffect.POISON)
                    {
                        enemy.tint.color.set(Color.CHARTREUSE);
                        enemy.tint.changeColor(Color.WHITE.cpy());
                    }
                    else if (this.attackEffect == AttackEffect.FIRE)
                    {
                        enemy.tint.color.set(Color.RED);
                        enemy.tint.changeColor(Color.WHITE.cpy());
                    }

                    DamageHelper.DealDamage(target, new DamageInfo(this.source, this.damage[i], this.damageType), bypassBlock, bypassThorns);
                }

                i += 1;
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameActionsHelper2.ClearPostCombatActions();
            }

            if (!Settings.FAST_MODE)
            {
                GameActionsHelper2.AddToTop(new WaitAction(0.1f));
            }

            Complete(targets);
        }
    }

    protected void DamageTarget(AbstractMonster target, DamageInfo info)
    {
        target.damage(info);
        targets.add(target);
    }
}
