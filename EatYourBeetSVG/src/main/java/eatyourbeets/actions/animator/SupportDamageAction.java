package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class SupportDamageAction extends EYBAction
{
    private final DamageInfo info;

    public SupportDamageAction(DamageInfo info)
    {
        this(info, FindLowestHPEnemy());
    }

    public SupportDamageAction(DamageInfo info, AbstractCreature target)
    {
        super(ActionType.DAMAGE);

        this.info = info;
        this.attackEffect = AttackEffect.NONE;

        Initialize(info.owner, target, info.output);
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return this.target == null || (this.source != null && this.source.isDying);
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.shouldCancelAction())
        {
            Complete();
            return;
        }

        AbstractCreature bestTarget = FindLowestHPEnemy();

        if (target != bestTarget || target.isDeadOrEscaped() || target.currentHealth <= 0)
        {
            if (GameUtilities.GetCurrentEnemies(true).size() > 0)
            {
                GameActions.Top.Add(new SupportDamageAction(info, bestTarget));
            }

            Complete();
            return;
        }

        this.target.damageFlash = true;
        this.target.damageFlashFrames = 4;
        GameEffects.List.Add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
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

            this.info.owner = null;
            this.target.damage(this.info);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                GameUtilities.ClearPostCombatActions();
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
