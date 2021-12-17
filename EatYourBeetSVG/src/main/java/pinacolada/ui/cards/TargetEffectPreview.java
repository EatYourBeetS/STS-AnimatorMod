package pinacolada.ui.cards;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;

public class TargetEffectPreview
{
    private final ActionT1<AbstractMonster> updateTarget;
    private AbstractMonster lastTarget = null;
    private AbstractMonster target = null;

    public TargetEffectPreview(ActionT1<AbstractMonster> updateTarget)
    {
        this.updateTarget = updateTarget;
    }

    public void Update()
    {
        if (lastTarget != target)
        {
            updateTarget.Invoke(target);
            lastTarget = target;
        }

        target = null;
    }

    public void SetCurrentTarget(AbstractMonster target)
    {
        this.target = target;
    }
}
