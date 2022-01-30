package pinacolada.cards.base.cardeffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffects.*;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public abstract class GenericEffect
{
    protected static final String SEPARATOR = "|";
    protected static final String SUB_SEPARATOR = ";";

    private static final HashMap<String, Class<? extends GenericEffect>> EFFECT_MAP = new HashMap<>();

    public static String Register(Class<? extends GenericEffect> type) {
        String id = GR.PCL.CreateID(type.getSimpleName());
        EFFECT_MAP.put(id, type);
        return id;
    }

    public static GenericEffect Get(String serializedString) {
        String[] content = PCLJUtils.SplitString(SEPARATOR, serializedString);
        Constructor<? extends GenericEffect> c = PCLJUtils.TryGetConstructor(EFFECT_MAP.get(content[0]), String[].class);
        if (c != null) {
            try {
                return c.newInstance((Object) content);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> String JoinEntityIDs(T[] items, FuncT1<String, T> stringFunction) {return PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction));}
    public static <T> String JoinEntityIDs(Collection<T> items, FuncT1<String, T> stringFunction) {return PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction));}
    public static String JoinEffectTexts(Collection<GenericEffect> effects) {
        return JoinEffectTexts(effects, " NL ");
    }
    public static String JoinEffectTexts(Collection<GenericEffect> effects, String delimiter) {
        return PCLJUtils.JoinStrings(" NL ", PCLJUtils.Filter(PCLJUtils.Map(effects, GenericEffect::GetText), Objects::nonNull));
    }

    public static GenericEffect Apply(int amount, PCLPowerHelper... powers) {
        return new GenericEffect_StackPower(PCLCardTarget.Normal, amount, powers);
    }

    public static GenericEffect ApplyToEnemies(int amount, PCLPowerHelper... powers) {
        return new GenericEffect_StackPower(PCLCardTarget.AoE, amount, powers);
    }

    public static GenericEffect ApplyToRandom(int amount, PCLPowerHelper... powers) {
        return new GenericEffect_StackPower(PCLCardTarget.Random, amount, powers);
    }

    public static GenericEffect ChannelOrb(int amount, PCLOrbHelper orb) {
        return new GenericEffect_ChannelOrb(amount, orb);
    }

    public static GenericEffect DealDamage(int amount) {
        return new GenericEffect_DealDamage(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static GenericEffect DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new GenericEffect_DealDamage(amount, attackEffect);
    }

    public static GenericEffect DealDamageToAll(int amount) {
        return new GenericEffect_DealDamageToAll(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static GenericEffect DealDamageToAll(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new GenericEffect_DealDamageToAll(amount, attackEffect);
    }

    public static GenericEffect Draw(int amount) {
        return new GenericEffect_Draw(amount);
    }

    public static GenericEffect EnterStance(PCLStanceHelper helper) {
        return new GenericEffect_EnterStance(helper);
    }

    public static GenericEffect Gain(int amount, PCLPowerHelper... powers) {
        return new GenericEffect_StackPower(PCLCardTarget.Self, amount, powers);
    }

    public static GenericEffect GainAffinity(int amount, PCLAffinity... affinities) {
        return new GenericEffect_GainAffinity(amount, affinities);
    }

    public static GenericEffect GainAffinityPower(int amount, PCLAffinity... affinities) {
        return new GenericEffect_GainAffinityPower(amount, affinities);
    }

    public static GenericEffect GainBlock(int amount) {
        return new GenericEffect_GainBlock(amount);
    }

    public static GenericEffect GainOrbSlots(int amount) {
        return new GenericEffect_GainOrbSlots(amount);
    }

    public static GenericEffect GainTempHP(int amount) {
        return new GenericEffect_GainTempHP(amount);
    }

    public static GenericEffect Obtain(PCLCardData... cardData) {
        return new GenericEffect_Obtain(1, 1, cardData);
    }

    public static GenericEffect Obtain(int copies, int upgradeTimes, PCLCardData... cardData) {
        return new GenericEffect_Obtain(copies, upgradeTimes, cardData);
    }

    public static GenericEffect PayAffinity(int amount, PCLAffinity... affinities) {
        return new GenericEffect_PayAffinity(amount, affinities);
    }

    public static GenericEffect Scry(int amount) {
        return new GenericEffect_Scry(amount);
    }

    public static GenericEffect TriggerOrb(int amount, PCLOrbHelper orb) {
        return new GenericEffect_TriggerOrb(amount, orb);
    }

    public String effectID;
    public String entityID;
    public PCLCardTarget target;
    public int amount;
    public int misc;

    public GenericEffect() {
        target = PCLCardTarget.None;
    }

    public GenericEffect(String[] content) {
        this.effectID = content[0];
        this.entityID = content[1];
        this.target = PCLCardTarget.valueOf(content[2]);
        this.amount = Integer.parseInt(content[3]);
        this.misc = Integer.parseInt(content[4]);
    }

    public GenericEffect(String effectID, String entityID, PCLCardTarget target, int amount) {
        this(effectID, entityID, target, amount, 0);
    }

    public GenericEffect(String effectID, String entityID, PCLCardTarget target, int amount, int misc) {
        this.effectID = effectID;
        this.entityID = entityID;
        this.target = target;
        this.amount = amount;
        this.misc = misc;
    }

    public final String[] SplitEntityIDs() {
        return PCLJUtils.SplitString(SUB_SEPARATOR, entityID);
    }
    public final String Serialize() {
        return PCLJUtils.JoinStrings(SEPARATOR, new String[] {
           effectID,
           entityID,
           target.name(),
           String.valueOf(amount),
           String.valueOf(misc)
        });
    }

    public abstract String GetText();
    public abstract void Use(PCLCard card, AbstractPlayer p, AbstractMonster m);
}
