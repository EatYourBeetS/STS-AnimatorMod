package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import eatyourbeets.utilities.JavaUtilities;

public class DieAction extends AbstractGameAction
{
    public DieAction(AbstractCreature target)
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
            if (!target.isDeadOrEscaped())
            {
                AbstractMonster m = JavaUtilities.SafeCast(target, AbstractMonster.class);
                if (m != null)
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
                else if (target instanceof AbstractPlayer)
                {
                    AbstractDungeon.player.isDead = true;
                    AbstractDungeon.player.currentHealth = 0;
                    AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
                }
            }
        }

        this.tickDuration();
    }
}
