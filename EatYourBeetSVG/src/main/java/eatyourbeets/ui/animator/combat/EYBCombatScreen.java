package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;

public class EYBCombatScreen extends GUIElement implements OnStatsClearedSubscriber
{
    public final EYBCombatInfo Info = new EYBCombatInfo();
    public final EnemySubIntents Intents = new EnemySubIntents();

    public EYBCombatScreen()
    {
        CombatStats.onStatsCleared.Subscribe(this);
        SetActive(false);
    }

    @Override
    public boolean OnStatsCleared()
    {
        SetActive(GameUtilities.InBattle());
        return false;
    }

    @Override
    public void Update()
    {
        Info.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        Info.TryRender(sb);
    }
}
