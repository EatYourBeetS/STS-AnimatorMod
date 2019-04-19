package eatyourbeets.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class PiercingDamageAction extends AbstractGameAction
{
    private final DamageInfo info;
    private int goldAmount;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private boolean skipWait;
    private boolean muteSfx;

    private final ArrayList<AbstractPower> ignoredPowers = new ArrayList<>();

    public PiercingDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect)
    {
        this.goldAmount = 0;
        this.skipWait = false;
        this.muteSfx = false;
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        addIgnoredPower(SharpHidePower.POWER_ID);
    }

    public PiercingDamageAction(AbstractCreature target, DamageInfo info, int stealGoldAmount)
    {
        this(target, info, AttackEffect.SLASH_DIAGONAL);
        this.goldAmount = stealGoldAmount;
    }

    public PiercingDamageAction(AbstractCreature target, DamageInfo info)
    {
        this(target, info, AttackEffect.NONE);
    }

    public PiercingDamageAction(AbstractCreature target, DamageInfo info, boolean superFast)
    {
        this(target, info, AttackEffect.NONE);
        this.skipWait = superFast;
    }

    public PiercingDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast)
    {
        this(target, info, effect);
        this.skipWait = superFast;
    }

    public PiercingDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast, boolean muteSfx)
    {
        this(target, info, effect, superFast);
        this.muteSfx = muteSfx;
    }

    public void update()
    {
        if (this.shouldCancelAction() && this.info.type != DamageType.THORNS)
        {
            this.isDone = true;
            reapplyIgnoredPowers();
        }
        else
        {
            if (this.duration == 0.1F)
            {
                if (this.info.type != DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead))
                {
                    this.isDone = true;
                    reapplyIgnoredPowers();
                    return;
                }

                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
                if (this.goldAmount != 0)
                {
                    this.stealGold();
                }
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


                int block = this.target.currentBlock;

                addIgnoredPower(ThornsPower.POWER_ID);
                addIgnoredPower(FlameBarrierPower.POWER_ID);
                addIgnoredPower(CurlUpPower.POWER_ID);

                this.target.currentBlock = 0;
                this.target.damage(this.info);

                if (this.target.currentHealth > 0)
                {
                    this.target.currentBlock = block;
                    reapplyIgnoredPowers();
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                if (!this.skipWait && !Settings.FAST_MODE)
                {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                }
            }
        }
    }

    private void reapplyIgnoredPowers()
    {
        this.target.powers.addAll(ignoredPowers);
    }

    private void addIgnoredPower(String powerID)
    {
        AbstractPower power = target.getPower(powerID);
        if (power != null)
        {
            ignoredPowers.add(power);
            this.target.powers.remove(power);
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

            AbstractCreature var10000 = this.target;
            var10000.gold -= this.goldAmount;

            for (int i = 0; i < this.goldAmount; ++i)
            {
                if (this.source.isPlayer)
                {
                    AbstractDungeon.effectList.add(new GainPennyEffect(this.target.hb.cX, this.target.hb.cY));
                }
                else
                {
                    AbstractDungeon.effectList.add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, false));
                }
            }

        }
    }
}