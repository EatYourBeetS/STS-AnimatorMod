package eatyourbeets.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

import java.util.function.Consumer;

public class DealDamageToRandomEnemy extends EYBActionWithCallback<AbstractCreature>
{
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean skipWait;

    protected final DamageInfo info;
    protected Consumer<AbstractCreature> onDamageEffect;

    protected DealDamageToRandomEnemy(DealDamageToRandomEnemy other)
    {
        this(other.info, other.attackEffect);

        this.skipWait = other.skipWait;
        this.bypassBlock = other.bypassBlock;
        this.bypassThorns = other.bypassThorns;
        this.onDamageEffect = other.onDamageEffect;
        this.callbacks = other.callbacks;
    }

    public DealDamageToRandomEnemy(DamageInfo info, AttackEffect effect)
    {
        super(ActionType.DAMAGE);

        this.info = info;
        this.attackEffect = effect;

        Initialize(player, GameUtilities.GetRandomEnemy(true), info.output);
    }

    public DealDamageToRandomEnemy SetDamageEffect(Consumer<AbstractCreature> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamageToRandomEnemy SetOptions(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamageToRandomEnemy SetOptions2(boolean superFast)
    {
        this.skipWait = superFast;

        return this;
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return this.target == null || this.source != null && this.source.isDying;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.shouldCancelAction())
        {
            Complete();
            return;
        }

        if (GameUtilities.IsDeadOrEscaped(target))
        {
            if (GameUtilities.GetCurrentEnemies(true).size() > 0)
            {
                GameActionsHelper.AddToTop(new DealDamageToRandomEnemy(this));
            }

            Complete();
            return;
        }

        this.target.damageFlash = true;
        this.target.damageFlashFrames = 4;
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));

        if (onDamageEffect != null)
        {
            onDamageEffect.accept(target);
        }
    }

    @Override
    protected void UpdateInternal()
    {
        if (this.shouldCancelAction())
        {
            Complete();
            return;
        }

        this.tickDuration();

        if (this.isDone)
        {
            if (this.attackEffect == AttackEffect.POISON)
            {
                this.target.tint.color = Color.CHARTREUSE.cpy();
                this.target.tint.changeColor(Color.WHITE.cpy());
            }
            else if (this.attackEffect == AttackEffect.FIRE)
            {
                this.target.tint.color = Color.RED.cpy();
                this.target.tint.changeColor(Color.WHITE.cpy());
            }

            this.info.applyPowers(this.info.owner, target);
            DamageHelper.DealDamage(target, info, bypassBlock, bypassThorns);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameActionsHelper2.ClearPostCombatActions();
            }

            if (!Settings.FAST_MODE && !skipWait)
            {
                GameActionsHelper2.AddToTop(new WaitAction(0.1f));
            }

            Complete(target);
        }
    }
}
