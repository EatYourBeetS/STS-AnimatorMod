package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class DieAction extends EYBAction
{
    public DieAction(AbstractCreature target)
    {
        super(ActionType.DAMAGE, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        Initialize(target, target, 1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!GameUtilities.IsDeadOrEscaped(target))
        {
            AbstractMonster m = JUtils.SafeCast(target, AbstractMonster.class);
            if (m != null)
            {
                m.currentHealth = 0;
                m.die();
                m.hideHealthBar();

                if (GameUtilities.AreMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.cleanCardQueue();
                    GameEffects.List.Add(new DeckPoofEffect(64f * Settings.scale, 64f * Settings.scale, true));
                    GameEffects.List.Add(new DeckPoofEffect((float) Settings.WIDTH - 64f * Settings.scale, 64f * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                }
            }
            else if (target instanceof AbstractPlayer)
            {
                player.isDead = true;
                player.currentHealth = 0;
                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            }
        }
    }
}
