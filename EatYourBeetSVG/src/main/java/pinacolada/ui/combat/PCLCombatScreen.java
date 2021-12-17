package pinacolada.ui.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.ui.GUIElement;
import pinacolada.utilities.PCLGameUtilities;

public class PCLCombatScreen extends GUIElement implements OnStatsClearedSubscriber, OnBattleStartSubscriber
{
    public final PCLAffinitySystem Affinities = PCLCombatStats.MatchingSystem;
    public final EnemySubIntents Intents = new EnemySubIntents();
    public final CombatHelper Helper = new CombatHelper();

    protected float delay = 0;

    public PCLCombatScreen()
    {
        PCLCombatStats.onStatsCleared.Subscribe(this);
        PCLCombatStats.onBattleStart.Subscribe(this);
        SetActive(false);
    }

    //@Formatter: off
    @Override public void OnBattleStart() { OnStatsCleared(); }
    @Override public void OnStatsCleared()
    {
        SetActive(PCLGameUtilities.InBattle() && GR.PCL.IsSelected());
        PCLCombatStats.onBattleStart.Subscribe(this);
        Affinities.SetActive(isActive);
        Intents.Clear();
        Helper.Clear();
    }
    //@Formatter: on

    @Override
    public void Update()
    {
        Affinities.TryUpdate();
        Helper.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        Affinities.TryRender(sb);
    }
}
