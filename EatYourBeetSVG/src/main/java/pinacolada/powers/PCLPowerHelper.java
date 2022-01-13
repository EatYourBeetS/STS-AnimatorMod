package pinacolada.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.*;
import pinacolada.powers.replacement.*;
import pinacolada.powers.temporary.*;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashMap;
import java.util.Map;

public class PCLPowerHelper extends eatyourbeets.powers.PowerHelper
{
    public enum Behavior {
        Permanent,
        SingleTurn,
        Temporary,
        TurnBased,
    }

    public static final Map<String, PCLPowerHelper> ALL = new HashMap<>();

    public static final PCLPowerHelper Blinded = new PCLPowerHelper(BlindedPower.POWER_ID, GR.Tooltips.Blinded, BlindedPower::new, Behavior.TurnBased, true);
    public static final PCLPowerHelper Burning = new PCLPowerHelper(BurningPower.POWER_ID, GR.Tooltips.Burning, BurningPower::new, Behavior.TurnBased,true);
    public static final PCLPowerHelper Constricted = new PCLPowerHelper(ConstrictedPower.POWER_ID, GR.Tooltips.Constricted, PCLConstrictedPower::new, Behavior.Permanent,true);
    public static final PCLPowerHelper Electrified = new PCLPowerHelper(ElectrifiedPower.POWER_ID, GR.Tooltips.Electrified, ElectrifiedPower::new, Behavior.TurnBased,true);
    public static final PCLPowerHelper Freezing = new PCLPowerHelper(FreezingPower.POWER_ID, GR.Tooltips.Freezing, FreezingPower::new, Behavior.TurnBased,true);
    public static final PCLPowerHelper LockOn = new PCLPowerHelper(LockOnPower.POWER_ID, GR.Tooltips.LockOn, PCLLockOnPower::new, Behavior.TurnBased,true);
    public static final PCLPowerHelper Poison = new PCLPowerHelper(PoisonPower.POWER_ID, GR.Tooltips.Poison, PoisonPower::new, Behavior.TurnBased,true);
    public static final PCLPowerHelper Rippled = new PCLPowerHelper(RippledPower.POWER_ID, GR.Tooltips.Rippled, RippledPower::new, Behavior.TurnBased,true);
    public static final PCLPowerHelper Shackles = new PCLPowerHelper(ShacklesPower.POWER_ID, GR.Tooltips.Shackles, ShacklesPower::new, Behavior.SingleTurn,true);
    public static final PCLPowerHelper Weak = new PCLPowerHelper(WeakPower.POWER_ID, GR.Tooltips.Weak, (o, s, a) -> new PCLWeakPower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,true);
    public static final PCLPowerHelper Vulnerable = new PCLPowerHelper(VulnerablePower.POWER_ID, GR.Tooltips.Vulnerable, (o, s, a) -> new PCLVulnerablePower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,true);
    public static final PCLPowerHelper Frail = new PCLPowerHelper(FrailPower.POWER_ID, GR.Tooltips.Frail, (o, s, a) -> new PCLFrailPower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,true);
    public static final PCLPowerHelper DelayedDamage = new PCLPowerHelper(DelayedDamagePower.POWER_ID, GR.Tooltips.DelayedDamage, (o, s, a) -> new DelayedDamagePower(o, a, AttackEffects.CLAW), Behavior.SingleTurn,true);

    public static final PCLPowerHelper Artifact = new PCLPowerHelper(ArtifactPower.POWER_ID, GR.Tooltips.Artifact, ArtifactPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Blur = new PCLPowerHelper(BlurPower.POWER_ID, GR.Tooltips.Blur, PCLBlurPower::new, Behavior.TurnBased,false);
    public static final PCLPowerHelper Dexterity = new PCLPowerHelper(DexterityPower.POWER_ID, GR.Tooltips.Dexterity, DexterityPower::new, Behavior.Permanent, false);
    public static final PCLPowerHelper Focus = new PCLPowerHelper(FocusPower.POWER_ID, GR.Tooltips.Focus, FocusPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Inspiration = new PCLPowerHelper(InspirationPower.POWER_ID, GR.Tooltips.Inspiration, InspirationPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Malleable = new PCLPowerHelper(MalleablePower.POWER_ID, GR.Tooltips.Malleable, MalleablePower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Metallicize = new PCLPowerHelper(MetallicizePower.POWER_ID, GR.Tooltips.Metallicize, PCLMetallicizePower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper PlatedArmor = new PCLPowerHelper(PlatedArmorPower.POWER_ID, GR.Tooltips.PlatedArmor, PCLPlatedArmorPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Resistance = new PCLPowerHelper(ResistancePower.POWER_ID, GR.Tooltips.Resistance, ResistancePower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Sorcery = new PCLPowerHelper(SorceryPower.POWER_ID, GR.Tooltips.Sorcery, SorceryPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Strength = new PCLPowerHelper(StrengthPower.POWER_ID, GR.Tooltips.Strength, StrengthPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper SupportDamage = new PCLPowerHelper(SupportDamagePower.POWER_ID, GR.Tooltips.SupportDamage, SupportDamagePower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper TemporaryArtifact = new PCLPowerHelper(TemporaryArtifactPower.POWER_ID, GR.Tooltips.Artifact, TemporaryArtifactPower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper TemporaryDexterity = new PCLPowerHelper(TemporaryDexterityPower.POWER_ID, GR.Tooltips.Dexterity, TemporaryDexterityPower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper TemporaryFocus = new PCLPowerHelper(TemporaryFocusPower.POWER_ID, GR.Tooltips.Focus, TemporaryFocusPower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper TemporaryMalleable = new PCLPowerHelper(TemporaryMalleablePower.POWER_ID, GR.Tooltips.Malleable, TemporaryMalleablePower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper TemporaryResistance = new PCLPowerHelper(TemporaryResistancePower.POWER_ID, GR.Tooltips.Resistance, TemporaryResistancePower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper TemporaryStrength = new PCLPowerHelper(TemporaryStrengthPower.POWER_ID, GR.Tooltips.Strength, TemporaryStrengthPower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper TemporaryThorns = new PCLPowerHelper(TemporaryThornsPower.POWER_ID, GR.Tooltips.Thorns, TemporaryThornsPower::new, Behavior.Temporary,false);
    public static final PCLPowerHelper Thorns = new PCLPowerHelper(ThornsPower.POWER_ID, GR.Tooltips.Thorns, ThornsPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Vitality = new PCLPowerHelper(VitalityPower.POWER_ID, GR.Tooltips.Vitality, VitalityPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper Vigor = new PCLPowerHelper(VigorPower.POWER_ID, GR.Tooltips.Vigor, VigorPower::new, Behavior.Permanent,false);
    public static final PCLPowerHelper CounterAttack = new PCLPowerHelper(CounterAttackPower.POWER_ID, GR.Tooltips.CounterAttack, (o, s, a) -> new CounterAttackPower(o, a), Behavior.SingleTurn,false);
    public static final PCLPowerHelper EnchantedArmor = new PCLPowerHelper(EnchantedArmorPower.POWER_ID, GR.Tooltips.EnchantedArmor, (o, s, a) -> new EnchantedArmorPower(o, a), Behavior.Permanent,false);
    public static final PCLPowerHelper Ritual = new PCLPowerHelper(RitualPower.POWER_ID, GR.Tooltips.Ritual, (o, s, a) -> new RitualPower(o, a, PCLGameUtilities.IsPlayer(o)), Behavior.Permanent,false);

    public PCLCardTooltip Tooltip;
    public final String ID;
    public final Behavior EndTurnBehavior;
    public final boolean IsDebuff;
    protected final FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2;
    protected final FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructorT3;

    public PCLPowerHelper(String powerID, PCLCardTooltip tooltip, FuncT2<AbstractPower, AbstractCreature, Integer> constructor, Behavior endTurnBehavior, boolean isDebuff)
    {
        super(powerID, tooltip, constructor);
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = constructor;
        this.constructorT3 = null;
        this.EndTurnBehavior = endTurnBehavior;
        this.IsDebuff = isDebuff;
    }

    public PCLPowerHelper(String powerID, PCLCardTooltip tooltip, FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructor, Behavior endTurnBehavior, boolean isDebuff)
    {
        super(powerID, tooltip, constructor);
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
