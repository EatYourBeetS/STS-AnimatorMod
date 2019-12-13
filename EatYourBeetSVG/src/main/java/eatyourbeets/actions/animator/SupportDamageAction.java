package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class SupportDamageAction extends AbstractGameAction
{
    private final DamageInfo info;

    public SupportDamageAction(DamageInfo info, AttackEffect effect)
    {
        this(info, effect, FindLowestHPEnemy());
    }

    public SupportDamageAction(DamageInfo info, AttackEffect effect, AbstractCreature target)
    {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return this.target == null || this.source != null && this.source.isDying;
    }

    public void update()
    {
        if (this.shouldCancelAction())
        {
            this.isDone = true;
        }
        else
        {
            if (this.duration == Settings.ACTION_DUR_FAST)
            {
                AbstractCreature bestTarget = FindLowestHPEnemy();

                if (target != bestTarget || target.isDeadOrEscaped() || target.currentHealth <= 0)
                {
                    if (GameUtilities.GetCurrentEnemies(true).size() > 0)
                    {
                        GameActions.Top.Add(new SupportDamageAction(info, attackEffect, bestTarget));
                    }

                    this.isDone = true;
                    return;
                }

                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                GameEffects.List.Add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
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

                this.info.type = DamageInfo.DamageType.NORMAL;
                this.info.applyPowers(this.info.owner, target);
                GameUtilities.UsePenNib();

                if (this.info.output < 10)
                {
                    GameEffects.List.Add(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()));
                }
                else
                {
                    GameEffects.List.Add(new DieDieDieEffect());
                }

                //this.info.effectType = DamageInfo.DamageType.THORNS;
                this.info.owner = null;
                this.target.damage(this.info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
    }

    private static AbstractMonster FindLowestHPEnemy()
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);

        AbstractMonster enemy = null;
        int minHealth = Integer.MAX_VALUE;
        for (AbstractMonster m : enemies)
        {
            if (m.currentHealth < minHealth)
            {
                minHealth = m.currentHealth;
                enemy = m;
            }
        }

        return enemy;
    }
}
