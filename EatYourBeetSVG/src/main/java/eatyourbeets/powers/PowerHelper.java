package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.powers.common.*;
import eatyourbeets.powers.replacement.ImprovedConstrictedPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;
import java.util.Map;

public class PowerHelper
{
    public static final Map<String, PowerHelper> ALL = new HashMap<>();

    public static final PowerHelper Poison = new PowerHelper(PoisonPower.POWER_ID, GR.Tooltips.Poison, PoisonPower::new);
    public static final PowerHelper Burning = new PowerHelper(BurningPower.POWER_ID, GR.Tooltips.Burning, BurningPower::new);
    public static final PowerHelper Freezing = new PowerHelper(FreezingPower.POWER_ID, GR.Tooltips.Freezing, FreezingPower::new);
    public static final PowerHelper Constricted = new PowerHelper(ConstrictedPower.POWER_ID, GR.Tooltips.Constricted, ImprovedConstrictedPower::new);
    public static final PowerHelper Blinded = new PowerHelper(BlindedPower.POWER_ID, GR.Tooltips.Blinded, BlindedPower::new);
    public static final PowerHelper LockOn = new PowerHelper(LockOnPower.POWER_ID, GR.Tooltips.LockOn, LockOnPower::new);

    public static final PowerHelper Weak = new PowerHelper(WeakPower.POWER_ID, GR.Tooltips.Weak, (o, s, a) -> new WeakPower(o, a, GameUtilities.IsMonster(s)));
    public static final PowerHelper Vulnerable = new PowerHelper(VulnerablePower.POWER_ID, GR.Tooltips.Vulnerable, (o, s, a) -> new VulnerablePower(o, a, GameUtilities.IsMonster(s)));
    public static final PowerHelper Frail = new PowerHelper(FrailPower.POWER_ID, GR.Tooltips.Frail, (o, s, a) -> new FrailPower(o, a, GameUtilities.IsMonster(s)));

    public static final PowerHelper Ritual = new PowerHelper(RitualPower.POWER_ID, null, (o, s, a) -> new RitualPower(o, a, GameUtilities.IsPlayer(o)));

    public static final PowerHelper Strength = new PowerHelper(StrengthPower.POWER_ID, null, StrengthPower::new);
    public static final PowerHelper Dexterity = new PowerHelper(DexterityPower.POWER_ID, null, DexterityPower::new);
    public static final PowerHelper Focus = new PowerHelper(FocusPower.POWER_ID, null, FocusPower::new);
    public static final PowerHelper Balance = new PowerHelper(BalancePower.POWER_ID, null, BalancePower::new);

    public static final PowerHelper PlatedArmor = new PowerHelper(PlatedArmorPower.POWER_ID, GR.Tooltips.PlatedArmor, PlatedArmorPower::new);
    public static final PowerHelper Metallicize = new PowerHelper(MetallicizePower.POWER_ID, GR.Tooltips.Metallicize, MetallicizePower::new);
    public static final PowerHelper Malleable = new PowerHelper(MalleablePower.POWER_ID, GR.Tooltips.Malleable, MalleablePower::new);
    public static final PowerHelper Regen = new PowerHelper(RegenPower.POWER_ID, null, RegenPower::new);

    public static final PowerHelper Artifact = new PowerHelper(ArtifactPower.POWER_ID, GR.Tooltips.Artifact, ArtifactPower::new);
    public static final PowerHelper Thorns = new PowerHelper(ThornsPower.POWER_ID, GR.Tooltips.Thorns, ThornsPower::new);
    public static final PowerHelper TemporaryThorns = new PowerHelper(EarthenThornsPower.POWER_ID, null, EarthenThornsPower::new);
    public static final PowerHelper SelfDamage = new PowerHelper(SelfDamagePower.POWER_ID, GR.Tooltips.SelfDamage, SelfDamagePower::new);
    public static final PowerHelper Shackles = new PowerHelper(ShacklesPower.POWER_ID, GR.Tooltips.Shackles, ShacklesPower::new);

    public final String ID;
    public EYBCardTooltip Tooltip;

    protected final FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2;
    protected final FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructorT3;

    public PowerHelper(String powerID, EYBCardTooltip tooltip, FuncT2<AbstractPower, AbstractCreature, Integer> constructor)
    {
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = constructor;
        this.constructorT3 = null;
    }

    public PowerHelper(String powerID, EYBCardTooltip tooltip, FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructor)
    {
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = null;
        this.constructorT3 = constructor;
    }

    public AbstractPower Create(AbstractCreature owner, AbstractCreature source, int amount)
    {
        if (constructorT2 != null)
        {
            return constructorT2.Invoke(owner, amount);
        }
        else if (constructorT3 != null)
        {
            return constructorT3.Invoke(owner, source, amount);
        }
        else
        {
            throw new RuntimeException("Do not create a PowerHelper with a null constructor.");
        }
    }
}
