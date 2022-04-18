package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;

public class EYBCombatScreen extends GUIElement implements OnStatsClearedSubscriber, OnBattleStartSubscriber
{
    public final EYBCardAffinitySystem Affinities = CombatStats.Affinities;
    public final UnnamedDollManager Dolls = CombatStats.Dolls;
    public final EnemySubIntents Intents = new EnemySubIntents();
    //public final MulliganSystem Mulligan = new MulliganSystem();
    public final CombatHelper Helper = new CombatHelper();

    protected float delay = 0;

    public EYBCombatScreen()
    {
        CombatStats.onStatsCleared.Subscribe(this);
        CombatStats.onBattleStart.Subscribe(this);
        SetActive(false);
    }

    //@Formatter: off
    @Override public void OnBattleStart() { OnStatsCleared(); }
    @Override public void OnStatsCleared()
    {
        SetActive(GameUtilities.InBattle() && GameUtilities.IsEYBPlayerClass());
        Affinities.SetActive(isActive && GR.Animator.IsSelected());
        Dolls.SetActive(isActive && GR.Unnamed.IsSelected());
        CombatStats.onBattleStart.Subscribe(this);

        Dolls.Clear();
        //Mulligan.Clear();
        Intents.Clear();
        Helper.Clear();
    }
    //@Formatter: on

    @Override
    public void Update()
    {
        Affinities.TryUpdate();
        Dolls.TryUpdate();
        //Mulligan.TryUpdate();
        Helper.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        //Dolls.TryRender happens on PreRender
        Affinities.TryRender(sb);
        //Mulligan.TryRender(sb);
    }
}
