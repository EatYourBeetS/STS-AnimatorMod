package eatyourbeets.actions._legacy.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class DamageAllEnemiesPiercingAction extends AbstractGameAction
{
    public boolean bypassBlock = true;
    public int[] damage;
    private boolean firstFrame;

    private final ArrayList<AbstractPower> ignoredPowers = new ArrayList<>();

    public DamageAllEnemiesPiercingAction(AbstractCreature source, int[] amount, DamageType type, AttackEffect effect, boolean isFast)
    {
        this.firstFrame = true;
        this.setValues(null, source, amount[0]);
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        if (isFast)
        {
            this.duration = Settings.ACTION_DUR_XFAST;
        }
        else
        {
            this.duration = Settings.ACTION_DUR_FAST;
        }

    }

    public DamageAllEnemiesPiercingAction(AbstractCreature source, int[] amount, DamageType type, AttackEffect effect)
    {
        this(source, amount, type, effect, false);
    }

    public void update()
    {
        int i;
        if (this.firstFrame)
        {
            boolean playedMusic = false;
            i = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for (int j = 0; j < i; ++j)
            {
                AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.monsters.get(j);

                if (!target.isDying && target.currentHealth > 0 && !target.isEscaping)
                {
                    if (playedMusic)
                    {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, this.attackEffect, true));
                    }
                    else
                    {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, this.attackEffect));
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone)
        {
            for (AbstractPower p : AbstractDungeon.player.powers)
            {
                p.onDamageAllEnemies(this.damage);
            }

            int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for (i = 0; i < temp; ++i)
            {
                AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!target.isDeadOrEscaped())
                {
                    if (this.attackEffect == AttackEffect.POISON)
                    {
                        target.tint.color.set(Color.CHARTREUSE);
                        target.tint.changeColor(Color.WHITE.cpy());
                    }
                    else if (this.attackEffect == AttackEffect.FIRE)
                    {
                        target.tint.color.set(Color.RED);
                        target.tint.changeColor(Color.WHITE.cpy());
                    }

                    ArrayList<AbstractPower> powers = new ArrayList<>();

                    int block = PiercingDamageAction.RemovePowers(powers, target, bypassBlock);

                    target.damage(new DamageInfo(this.source, this.damage[i], this.damageType));

                    PiercingDamageAction.ReapplyPowers(powers, target, block);
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE)
            {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }

    }
}
