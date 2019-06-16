package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;

public class DieAction extends AbstractGameAction
{
    public DieAction(AbstractMonster target)
    {
        if (Settings.FAST_MODE)
        {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        }
        else
        {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration;
        this.target = target;
    }

    public void update()
    {
        if (this.duration == this.startDuration)
        {
            AbstractMonster m = (AbstractMonster) this.target;
            if (!m.isDeadOrEscaped())
            {
                m.currentHealth = 0;
                m.die();

                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.cleanCardQueue();
                    AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                    AbstractDungeon.effectList.add(new DeckPoofEffect((float) Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                }
            }
        }

        this.tickDuration();
    }
}
