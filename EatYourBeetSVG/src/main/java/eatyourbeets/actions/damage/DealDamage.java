package eatyourbeets.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.function.Consumer;

public class DealDamage extends EYBActionWithCallback<AbstractCreature>
{
    protected final DamageInfo info;

    protected Consumer<AbstractCreature> onDamageEffect;
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean skipWait;
    protected boolean muteSfx;
    protected int goldAmount;

    public DealDamage(AbstractCreature target, DamageInfo info)
    {
        this(target, info, AttackEffect.NONE);
    }

    public DealDamage(AbstractCreature target, DamageInfo info, AttackEffect effect)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        this.goldAmount = 0;
        this.skipWait = false;
        this.muteSfx = false;
        this.info = info;
        this.attackEffect = effect;

        Initialize(info.owner, target, info.output);
    }

    public DealDamage SetDamageEffect(Consumer<AbstractCreature> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamage SetOptions(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamage SetOptions2(boolean superFast, boolean muteSfx, int stealGoldAmount)
    {
        this.skipWait = superFast;
        this.muteSfx = muteSfx;
        this.goldAmount = stealGoldAmount;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.info.type != DamageInfo.DamageType.THORNS && (this.shouldCancelAction() || this.info.owner.isDying || this.info.owner.halfDead))
        {
            Complete();
            return;
        }

        this.target.damageFlash = true;
        this.target.damageFlashFrames = 4;
        GameEffects.List.Add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));

        if (onDamageEffect != null)
        {
            onDamageEffect.accept(target);
        }

        if (this.goldAmount != 0)
        {
            this.stealGold();
        }
    }

    @Override
    protected void UpdateInternal()
    {
        if (this.info.type != DamageInfo.DamageType.THORNS && this.shouldCancelAction())
        {
            Complete();
            return;
        }

        tickDuration();

        if (this.isDone)
        {
            if (this.attackEffect == AttackEffect.POISON)
            {
                this.target.tint.color.set(Color.CHARTREUSE.cpy());
                this.target.tint.changeColor(Color.WHITE.cpy());
            }
            else if (this.attackEffect == AttackEffect.FIRE)
            {
                this.target.tint.color.set(Color.RED);
                this.target.tint.changeColor(Color.WHITE.cpy());
            }

            DamageHelper.DealDamage(target, info, bypassBlock, bypassThorns);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameUtilities.ClearPostCombatActions();
            }

            if (!this.skipWait && !Settings.FAST_MODE)
            {
                GameActions.Top.Wait(0.1f);
            }

            Complete(target);
        }
    }

    private void stealGold()
    {
        if (this.target.gold != 0)
        {
            CardCrawlGame.sound.play("GOLD_JINGLE");
            if (this.target.gold < this.goldAmount)
            {
                this.goldAmount = this.target.gold;
            }

            target.gold -= this.goldAmount;

            for (int i = 0; i < this.goldAmount; ++i)
            {
                if (this.source.isPlayer)
                {
                    GameEffects.List.Add(new GainPennyEffect(this.target.hb.cX, this.target.hb.cY));
                }
                else
                {
                    GameEffects.List.Add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, false));
                }
            }
        }
    }
}
