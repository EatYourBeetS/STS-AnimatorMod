package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;

public class EYBCombatScreen extends GUIElement implements OnStatsClearedSubscriber, OnBattleStartSubscriber, OnBattleEndSubscriber
{
    public final EYBCombatInfo Info = new EYBCombatInfo();
    public final EnemySubIntents Intents = new EnemySubIntents();

    protected float delay = 0;

    public EYBCombatScreen()
    {
        CombatStats.onStatsCleared.Subscribe(this);
        CombatStats.onBattleStart.Subscribe(this);
        CombatStats.onBattleEnd.Subscribe(this);
        SetActive(false);
    }

    //@Formatter: off
    @Override public void OnBattleEnd() { OnStatsCleared(); }
    @Override public void OnBattleStart() { OnStatsCleared(); }
    @Override
    public boolean OnStatsCleared()
    {
        SetActive(GameUtilities.InBattle());
        Info.SetActive(isActive);
        return false;
    }
    //@Formatter: on

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
