package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.*;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import pinacolada.orbs.pcl.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLJUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Amplification_AbstractPower extends PCLPower implements OnOrbApplyFocusSubscriber
{
    protected static final HashMap<String, TextureCache> IMAGES = new HashMap<>();

    public final String orbID;
    public final int evokeMultiplier;
    public final HashSet<PCLAffinity> affinities = new HashSet<>();
    protected String cachedName = "";

    static {
        IMAGES.put(Air.ORB_ID, GR.PCL.Images.Tooltips.Air);
        IMAGES.put(Chaos.ORB_ID, GR.PCL.Images.Tooltips.Chaos);
        IMAGES.put(Dark.ORB_ID, GR.PCL.Images.Tooltips.Dark);
        IMAGES.put(Earth.ORB_ID, GR.PCL.Images.Tooltips.Earth);
        IMAGES.put(Fire.ORB_ID, GR.PCL.Images.Tooltips.Fire);
        IMAGES.put(Frost.ORB_ID, GR.PCL.Images.Tooltips.Frost);
        IMAGES.put(Lightning.ORB_ID, GR.PCL.Images.Tooltips.Lightning);
        IMAGES.put(Plasma.ORB_ID, GR.PCL.Images.Tooltips.Plasma);
        IMAGES.put(Water.ORB_ID, GR.PCL.Images.Tooltips.Water);
    }

    public Amplification_AbstractPower(AbstractCreature owner, String powerID, String orbID, int scaling, int evokeMultiplier, PCLAffinity... affinities)
    {
        super(owner, powerID);

        this.orbID = orbID;
        this.affinities.addAll(Arrays.asList(affinities));
        this.evokeMultiplier = evokeMultiplier;

        TextureCache tc = IMAGES.get(orbID);
        if (tc != null) {
            this.img = tc.Texture();
        }

        UpdateCachedName();

        Initialize(scaling);
        refreshOrbs();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onOrbApplyFocus.Subscribe(this);
    }


    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onOrbApplyFocus.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount, cachedName, this.evokeMultiplier * amount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        refreshOrbs();
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb)
    {
        if (orb != null && orb.ID.equals(orbID)) {
            orb.passiveAmount += GetScaledIncrease();
            orb.evokeAmount += this.evokeMultiplier * GetScaledIncrease();
        }
    }

    public void AddAffinity(PCLAffinity affinity) {
        affinities.add(affinity);
        UpdateCachedName();
    }

    protected float GetScaledIncrease() {
        return PCLJUtils.Sum(affinities, affinity -> Float.valueOf(PCLCombatStats.MatchingSystem.GetPowerLevel(affinity) * amount));
    }

    protected void UpdateCachedName() {
        cachedName = PCLJUtils.JoinStrings(", ", PCLJUtils.Map(Arrays.asList(this.affinities.toArray()), affinity -> PCLCombatStats.MatchingSystem.GetPower((PCLAffinity) affinity).name));
    }

    protected void refreshOrbs() {
        for (AbstractOrb orb : player.orbs) {
            if (orb != null && !(orb instanceof EmptyOrbSlot)) {
                orb.applyFocus();
            }
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        refreshOrbs();
    }

}