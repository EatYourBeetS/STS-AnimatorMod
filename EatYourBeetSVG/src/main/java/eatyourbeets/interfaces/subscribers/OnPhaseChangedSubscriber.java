package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.actions.GameActionManager;

public interface OnPhaseChangedSubscriber
{
    void OnPhaseChanged(GameActionManager.Phase phase);
}