package pinacolada.orbs;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.Map;

public class PCLOrbHelper
{
    public static final int COMMON_THRESHOLD = 11;
    public static final Map<String, PCLOrbHelper> ALL = new HashMap<>();
    protected static final WeightedList<PCLOrbHelper> WEIGHTED = new WeightedList<>();

    public static final PCLOrbHelper Air = new PCLOrbHelper(pinacolada.orbs.pcl.Air.ORB_ID, GR.Tooltips.Air, PCLAffinity.Green, pinacolada.orbs.pcl.Air::new, 7);
    public static final PCLOrbHelper Chaos = new PCLOrbHelper(pinacolada.orbs.pcl.Chaos.ORB_ID, GR.Tooltips.Chaos, PCLAffinity.Star, pinacolada.orbs.pcl.Chaos::new, 2);
    public static final PCLOrbHelper Dark = new PCLOrbHelper(com.megacrit.cardcrawl.orbs.Dark.ORB_ID, GR.Tooltips.Dark, PCLAffinity.Dark, com.megacrit.cardcrawl.orbs.Dark::new, COMMON_THRESHOLD);
    public static final PCLOrbHelper Earth = new PCLOrbHelper(pinacolada.orbs.pcl.Earth.ORB_ID, GR.Tooltips.Earth, PCLAffinity.Orange, pinacolada.orbs.pcl.Earth::new, 7);
    public static final PCLOrbHelper Fire = new PCLOrbHelper(pinacolada.orbs.pcl.Fire.ORB_ID, GR.Tooltips.Fire, PCLAffinity.Red, pinacolada.orbs.pcl.Fire::new, COMMON_THRESHOLD);
    public static final PCLOrbHelper Frost = new PCLOrbHelper(com.megacrit.cardcrawl.orbs.Frost.ORB_ID, GR.Tooltips.Frost, PCLAffinity.Blue, com.megacrit.cardcrawl.orbs.Frost::new, COMMON_THRESHOLD);
    public static final PCLOrbHelper Lightning = new PCLOrbHelper(com.megacrit.cardcrawl.orbs.Lightning.ORB_ID, GR.Tooltips.Lightning, PCLAffinity.Light, com.megacrit.cardcrawl.orbs.Lightning::new, COMMON_THRESHOLD);
    public static final PCLOrbHelper Metal = new PCLOrbHelper(pinacolada.orbs.pcl.Air.ORB_ID, GR.Tooltips.Metal, PCLAffinity.Silver, pinacolada.orbs.pcl.Metal::new, 2);
    public static final PCLOrbHelper Plasma = new PCLOrbHelper(com.megacrit.cardcrawl.orbs.Plasma.ORB_ID, GR.Tooltips.Plasma, PCLAffinity.Silver, com.megacrit.cardcrawl.orbs.Plasma::new, 2);
    public static final PCLOrbHelper Water = new PCLOrbHelper(pinacolada.orbs.pcl.Air.ORB_ID, GR.Tooltips.Water, PCLAffinity.Blue, pinacolada.orbs.pcl.Water::new, 2);

    public final PCLCardTooltip Tooltip;
    public final PCLAffinity Affinity;
    public final String ID;
    public final int weight;
    protected final FuncT0<AbstractOrb> constructor;

    public static PCLOrbHelper RandomCommonHelper() {
        return PCLGameUtilities.GetRandomElement(PCLJUtils.Filter(WEIGHTED.GetInnerList(), PCLOrbHelper::IsCommon));
    }

    public static AbstractOrb RandomCommonOrb() {
        return RandomCommonHelper().Create();
    }

    public static PCLOrbHelper RandomHelper() {
        return RandomHelper(true);
    }

    public static PCLOrbHelper RandomHelper(boolean weighted) {
        return weighted ? WEIGHTED.Retrieve(PCLGameUtilities.GetRNG(), false) : PCLGameUtilities.GetRandomElement(WEIGHTED.GetInnerList());
    }

    public static AbstractOrb RandomOrb() {
        return RandomOrb(true);
    }

    public static AbstractOrb RandomOrb(boolean weighted) {
       return RandomHelper(weighted).Create();
    }

    public PCLOrbHelper(String powerID, PCLCardTooltip tooltip, PCLAffinity affinity, FuncT0<AbstractOrb> constructor, int weight)
    {
        this.ID = powerID;
        this.Tooltip = tooltip;
        this.Affinity = affinity;
        this.constructor = constructor;
        this.weight = weight;

        ALL.putIfAbsent(powerID, this);
        WEIGHTED.Add(this, weight);
    }

    public AbstractOrb Create()
    {
        if (constructor != null)
        {
            return constructor.Invoke();
        }
        else
        {
            throw new RuntimeException("Do not create a PowerHelper with a null constructor.");
        }
    }

    public final boolean IsCommon() {
        return weight >= COMMON_THRESHOLD;
    }
}
