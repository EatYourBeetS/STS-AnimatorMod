package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.powers.common.EnergizedPower;
import eatyourbeets.powers.common.*;
import eatyourbeets.powers.replacement.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;
import java.util.Map;

public class PowerHelper
{
    public enum Behavior {
        Permanent,
        Temporary,
        TurnBased,
    }

    public static final Map<String, PowerHelper> ALL = new HashMap<>();

    public static final PowerHelper Blinded = new PowerHelper(BlindedPower.POWER_ID, GR.Tooltips.Blinded, BlindedPower::new, Behavior.TurnBased, true);
    public static final PowerHelper Burning = new PowerHelper(BurningPower.POWER_ID, GR.Tooltips.Burning, BurningPower::new, Behavior.TurnBased,true);
    public static final PowerHelper Constricted = new PowerHelper(ConstrictedPower.POWER_ID, GR.Tooltips.Constricted, AnimatorConstrictedPower::new, Behavior.Permanent,true);
    public static final PowerHelper Electrified = new PowerHelper(ElectrifiedPower.POWER_ID, GR.Tooltips.Freezing, ElectrifiedPower::new, Behavior.TurnBased,true);
    public static final PowerHelper Freezing = new PowerHelper(FreezingPower.POWER_ID, GR.Tooltips.Freezing, FreezingPower::new, Behavior.TurnBased,true);
    public static final PowerHelper LockOn = new PowerHelper(LockOnPower.POWER_ID, GR.Tooltips.LockOn, AnimatorLockOnPower::new, Behavior.TurnBased,true);
    public static final PowerHelper Poison = new PowerHelper(PoisonPower.POWER_ID, GR.Tooltips.Poison, PoisonPower::new, Behavior.TurnBased,true);
    public static final PowerHelper Shackles = new PowerHelper(ShacklesPower.POWER_ID, GR.Tooltips.Shackles, ShacklesPower::new, Behavior.Temporary,true);
    public static final PowerHelper Tainted = new PowerHelper(TaintedPower.POWER_ID, GR.Tooltips.Tainted, TaintedPower::new, Behavior.TurnBased,true);
    public static final PowerHelper Weak = new PowerHelper(WeakPower.POWER_ID, GR.Tooltips.Weak, (o, s, a) -> new AnimatorWeakPower(o, a, GameUtilities.IsMonster(s)), Behavior.TurnBased,true);
    public static final PowerHelper Vulnerable = new PowerHelper(VulnerablePower.POWER_ID, GR.Tooltips.Vulnerable, (o, s, a) -> new AnimatorVulnerablePower(o, a, GameUtilities.IsMonster(s)), Behavior.TurnBased,true);
    public static final PowerHelper Frail = new PowerHelper(FrailPower.POWER_ID, GR.Tooltips.Frail, (o, s, a) -> new AnimatorFrailPower(o, a, GameUtilities.IsMonster(s)), Behavior.TurnBased,true);
    public static final PowerHelper DelayedDamage = new PowerHelper(DelayedDamagePower.POWER_ID, GR.Tooltips.DelayedDamage, (o, s, a) -> new DelayedDamagePower(o, a, AttackEffects.CLAW), Behavior.Temporary,true);

    public static final PowerHelper Artifact = new PowerHelper(ArtifactPower.POWER_ID, GR.Tooltips.Artifact, ArtifactPower::new, Behavior.Permanent,false);
    public static final PowerHelper Blur = new PowerHelper(AnimatorBlurPower.POWER_ID, null, AnimatorBlurPower::new, Behavior.TurnBased,false);
    public static final PowerHelper CounterAttack = new PowerHelper(CounterAttackPower.POWER_ID, null, CounterAttackPower::new, Behavior.Temporary,false);
    public static final PowerHelper Dexterity = new PowerHelper(DexterityPower.POWER_ID, null, DexterityPower::new, Behavior.Permanent, false);
    public static final PowerHelper DrawCardNextTurn = new PowerHelper(DrawCardNextTurnPower.POWER_ID, null, DrawCardNextTurnPower::new, Behavior.Temporary,false);
    public static final PowerHelper Energized = new PowerHelper(EnergizedPower.POWER_ID, null, EnergizedPower::new, Behavior.Temporary,false);
    public static final PowerHelper Focus = new PowerHelper(FocusPower.POWER_ID, null, FocusPower::new, Behavior.Permanent,false);
    public static final PowerHelper Genesis = new PowerHelper(GenesisPower.POWER_ID, null, GenesisPower::new, Behavior.Permanent,false);
    public static final PowerHelper Inspiration = new PowerHelper(InspirationPower.POWER_ID, null, InspirationPower::new, Behavior.Permanent,false);
    public static final PowerHelper Intangible = new PowerHelper(IntangiblePlayerPower.POWER_ID, null, IntangiblePlayerPower::new, Behavior.TurnBased,false);
    public static final PowerHelper Malleable = new PowerHelper(MalleablePower.POWER_ID, GR.Tooltips.Malleable, MalleablePower::new, Behavior.Permanent,false);
    public static final PowerHelper Metallicize = new PowerHelper(MetallicizePower.POWER_ID, GR.Tooltips.Metallicize, AnimatorMetallicizePower::new, Behavior.Permanent,false);
    public static final PowerHelper PlatedArmor = new PowerHelper(PlatedArmorPower.POWER_ID, GR.Tooltips.PlatedArmor, AnimatorPlatedArmorPower::new, Behavior.Permanent,false);
    public static final PowerHelper Regen = new PowerHelper(RegenPower.POWER_ID, null, RegenPower::new, Behavior.TurnBased,false);
    public static final PowerHelper Resistance = new PowerHelper(ResistancePower.POWER_ID, null, ResistancePower::new, Behavior.Permanent,false);
    public static final PowerHelper Strength = new PowerHelper(StrengthPower.POWER_ID, null, StrengthPower::new, Behavior.Permanent,false);
    public static final PowerHelper SupportDamage = new PowerHelper(SupportDamagePower.POWER_ID, GR.Tooltips.SupportDamage, SupportDamagePower::new, Behavior.Permanent,false);
    public static final PowerHelper TemporaryArtifact = new PowerHelper(TemporaryArtifactPower.POWER_ID, GR.Tooltips.Artifact, TemporaryArtifactPower::new, Behavior.Temporary,false);
    public static final PowerHelper TemporaryDexterity = new PowerHelper(TemporaryDexterityPower.POWER_ID, null, TemporaryDexterityPower::new, Behavior.Temporary,false);
    public static final PowerHelper TemporaryFocus = new PowerHelper(TemporaryFocusPower.POWER_ID, null, TemporaryFocusPower::new, Behavior.Temporary,false);
    public static final PowerHelper TemporaryResistance = new PowerHelper(TemporaryResistancePower.POWER_ID, null, TemporaryResistancePower::new, Behavior.Temporary,false);
    public static final PowerHelper TemporaryStrength = new PowerHelper(TemporaryStrengthPower.POWER_ID, null, TemporaryStrengthPower::new, Behavior.Temporary,false);
    public static final PowerHelper TemporaryThorns = new PowerHelper(TemporaryThornsPower.POWER_ID, null, TemporaryThornsPower::new, Behavior.Temporary,false);
    public static final PowerHelper Thorns = new PowerHelper(ThornsPower.POWER_ID, GR.Tooltips.Thorns, ThornsPower::new, Behavior.Permanent,false);
    public static final PowerHelper Vitality = new PowerHelper(VitalityPower.POWER_ID, null, VitalityPower::new, Behavior.Permanent,false);
    public static final PowerHelper Vigor = new PowerHelper(VigorPower.POWER_ID, null, VigorPower::new, Behavior.Permanent,false);
    public static final PowerHelper EnchantedArmor = new PowerHelper(EnchantedArmorPower.POWER_ID, null, (o, s, a) -> new EnchantedArmorPower(o, a), Behavior.Permanent,false);
    public static final PowerHelper Ritual = new PowerHelper(RitualPower.POWER_ID, null, (o, s, a) -> new RitualPower(o, a, GameUtilities.IsPlayer(o)), Behavior.Permanent,false);

    public EYBCardTooltip Tooltip;
    public final String ID;
    public final Behavior EndTurnBehavior;
    public final boolean IsDebuff;
    protected final FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2;
    protected final FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructorT3;

    public PowerHelper(String powerID, EYBCardTooltip tooltip, FuncT2<AbstractPower, AbstractCreature, Integer> constructor, Behavior endTurnBehavior, boolean isDebuff)
    {
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = constructor;
        this.constructorT3 = null;
        this.EndTurnBehavior = endTurnBehavior;
        this.IsDebuff = isDebuff;
    }

    public PowerHelper(String powerID, EYBCardTooltip tooltip, FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructor, Behavior endTurnBehavior, boolean isDebuff)
    {
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = null;
        this.constructorT3 = constructor;
        this.EndTurnBehavior = endTurnBehavior;
        this.IsDebuff = isDebuff;
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
