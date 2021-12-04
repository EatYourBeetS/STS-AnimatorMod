package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.*;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.orbs.animator.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.JUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Amplification_AbstractPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
{
    protected static final HashMap<String, TextureCache> IMAGES = new HashMap<>();

    public final String orbID;
    public final int evokeMultiplier;
    public final HashSet<Affinity> affinities = new HashSet<>();
    protected String cachedName = "";

    static {
        IMAGES.put(Air.ORB_ID, GR.Common.Images.Tooltips.Air);
        IMAGES.put(Chaos.ORB_ID, GR.Common.Images.Tooltips.Chaos);
        IMAGES.put(Dark.ORB_ID, GR.Common.Images.Tooltips.Dark);
        IMAGES.put(Earth.ORB_ID, GR.Common.Images.Tooltips.Earth);
        IMAGES.put(Fire.ORB_ID, GR.Common.Images.Tooltips.Fire);
        IMAGES.put(Frost.ORB_ID, GR.Common.Images.Tooltips.Frost);
        IMAGES.put(Lightning.ORB_ID, GR.Common.Images.Tooltips.Lightning);
        IMAGES.put(Plasma.ORB_ID, GR.Common.Images.Tooltips.Plasma);
        IMAGES.put(Water.ORB_ID, GR.Common.Images.Tooltips.Water);
    }

    public Amplification_AbstractPower(AbstractCreature owner, String powerID, String orbID, int scaling, int evokeMultiplier, Affinity... affinities)
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

        CombatStats.onOrbApplyFocus.Subscribe(this);
    }


    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onOrbApplyFocus.Unsubscribe(this);
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

    public void AddAffinity(Affinity affinity) {
        affinities.add(affinity);
        UpdateCachedName();
    }

    protected float GetScaledIncrease() {
        return JUtils.Sum(affinities,affinity -> Float.valueOf(CombatStats.Affinities.GetPowerAmount(affinity) * amount));
    }

    protected void UpdateCachedName() {
        cachedName = JUtils.JoinStrings(", ", JUtils.Map(Arrays.asList(this.affinities.toArray()), affinity -> CombatStats.Affinities.GetPower((Affinity) affinity).name));
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