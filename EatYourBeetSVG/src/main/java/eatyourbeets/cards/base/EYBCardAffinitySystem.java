package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.*;

import java.util.ArrayList;

public class EYBCardAffinitySystem implements OnStartOfTurnSubscriber
{
    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public ForcePower Force;
    public AgilityPower Agility;
    public IntellectPower Intellect;
    public BlessingPower Blessing;
    public CorruptionPower Corruption;

    public EYBCardAffinitySystem()
    {
        Powers.add(Force = new ForcePower(null, 0));
        Powers.add(Agility = new AgilityPower(null, 0));
        Powers.add(Intellect = new IntellectPower(null, 0));
        Powers.add(Blessing = new BlessingPower(null, 0));
        Powers.add(Corruption = new CorruptionPower(null, 0));
    }

    public void Initialize()
    {
        for (AbstractAffinityPower p : Powers)
        {
            p.Initialize(AbstractDungeon.player);
        }

        CombatStats.onStartOfTurn.Subscribe(this);
    }

    public AbstractAffinityPower GetPower(AffinityType type)
    {
        for (AbstractAffinityPower p : Powers)
        {
            if (p.affinityType.equals(type))
            {
                return p;
            }
        }

        return null;
    }

    @Override
    public void OnStartOfTurn()
    {
        for (AbstractAffinityPower p : Powers)
        {
            p.atStartOfTurn();
            p.retained = false;
        }
    }
}