package eatyourbeets.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class DamageRandomEnemy2Action extends AbstractGameAction
{
    private final DamageInfo info;

    public DamageRandomEnemy2Action(DamageInfo info, AttackEffect effect)
    {
        this.info = info;
        this.setValues(PlayerStatistics.GetRandomEnemy(true), info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
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
            if (this.duration == 0.1F)
            {
                if (target.isDeadOrEscaped() || target.currentHealth <= 0)
                {
                    if (PlayerStatistics.GetCurrentEnemies(true).size() > 0)
                    {
                        GameActionsHelper.AddToTop(new DamageRandomEnemy2Action(info, attackEffect));
                    }

                    this.isDone = true;
                    return;
                }

                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
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

                this.target.damage(this.info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
