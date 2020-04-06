package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;
import java.util.Map;

public class PowerHelper
{
    public static final Map<String, PowerHelper> ALL = new HashMap<>();

    public static final PowerHelper Poison = new PowerHelper(PoisonPower.POWER_ID, PoisonPower::new);
    public static final PowerHelper Burning = new PowerHelper(BurningPower.POWER_ID, BurningPower::new);
    public static final PowerHelper Constricted = new PowerHelper(ConstrictedPower.POWER_ID, ConstrictedPower::new);

    public static final PowerHelper Weak = new PowerHelper(WeakPower.POWER_ID, (o, s, a) -> new WeakPower(o, a, GameUtilities.IsMonster(s)));
    public static final PowerHelper Vulnerable = new PowerHelper(VulnerablePower.POWER_ID, (o, s, a) -> new VulnerablePower(o, a, GameUtilities.IsMonster(s)));
    public static final PowerHelper Frail = new PowerHelper(FrailPower.POWER_ID, (o, s, a) -> new FrailPower(o, a, GameUtilities.IsMonster(s)));

    public static final PowerHelper Ritual = new PowerHelper(RitualPower.POWER_ID, (o, s, a) -> new RitualPower(o, a, GameUtilities.IsPlayer(o)));

    public static final PowerHelper Strength = new PowerHelper(StrengthPower.POWER_ID, StrengthPower::new);
    public static final PowerHelper Dexterity = new PowerHelper(DexterityPower.POWER_ID, DexterityPower::new);
    public static final PowerHelper Focus = new PowerHelper(FocusPower.POWER_ID, FocusPower::new);

    public static final PowerHelper PlatedArmor = new PowerHelper(PlatedArmorPower.POWER_ID, PlatedArmorPower::new);
    public static final PowerHelper Metallicize = new PowerHelper(MetallicizePower.POWER_ID, MetallicizePower::new);
    public static final PowerHelper Regen = new PowerHelper(RegenPower.POWER_ID, RegenPower::new);

    public static final PowerHelper Artifact = new PowerHelper(ArtifactPower.POWER_ID, ArtifactPower::new);
    public static final PowerHelper Thorns = new PowerHelper(ThornsPower.POWER_ID, ThornsPower::new);
    public static final PowerHelper TemporaryThorns = new PowerHelper(EarthenThornsPower.POWER_ID, EarthenThornsPower::new);

    protected final String ID;
    protected final FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2;
    protected final FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructorT3;

    public PowerHelper(String powerID, FuncT2<AbstractPower, AbstractCreature, Integer> constructor)
    {
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.constructorT2 = constructor;
        this.constructorT3 = null;
    }

    public PowerHelper(String powerID, FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructor)
    {
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
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
