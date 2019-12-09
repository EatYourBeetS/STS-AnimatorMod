//package eatyourbeets.utilities;
//
//import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.animations.VFXAction;
//import com.megacrit.cardcrawl.actions.common.*;
//import com.megacrit.cardcrawl.actions.defect.ChannelAction;
//import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
//import com.megacrit.cardcrawl.actions.utility.SFXAction;
//import com.megacrit.cardcrawl.actions.utility.WaitAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.cards.CardGroup;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.orbs.AbstractOrb;
//import com.megacrit.cardcrawl.powers.*;
//import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
//import eatyourbeets.actions.basic.*;
//import eatyourbeets.actions.cardManipulation.MakeTempCard;
//import eatyourbeets.actions.cardManipulation.ModifyAllCombatInstances;
//import eatyourbeets.actions.cardManipulation.MotivateAction;
//import eatyourbeets.actions.damage.DealDamage;
//import eatyourbeets.actions.damage.DealDamageToAll;
//import eatyourbeets.actions.damage.DealDamageToRandomEnemy;
//import eatyourbeets.actions.handSelection.DiscardFromHand;
//import eatyourbeets.actions.handSelection.ExhaustFromHand;
//import eatyourbeets.actions.handSelection.SelectFromHand;
//import eatyourbeets.actions.pileSelection.DiscardFromPile;
//import eatyourbeets.actions.pileSelection.ExhaustFromPile;
//import eatyourbeets.actions.pileSelection.FetchFromPile;
//import eatyourbeets.actions.pileSelection.SelectFromPile;
//import eatyourbeets.actions.powers.ApplyPowerSilently;
//import eatyourbeets.actions.powers.ReduceStrength;
//import eatyourbeets.actions.special.CreateThrowingKnives;
//import eatyourbeets.actions.utility.CallbackAction;
//import eatyourbeets.actions.utility.SequentialAction;
//import eatyourbeets.actions.utility.WaitRealtimeAction;
//import eatyourbeets.powers.animator.BurningPower;
//import eatyourbeets.powers.animator.EarthenThornsPower;
//import eatyourbeets.powers.common.AgilityPower;
//import eatyourbeets.powers.common.ForcePower;
//import eatyourbeets.powers.common.IntellectPower;
//import eatyourbeets.powers.common.TemporaryArtifactPower;
//
//import java.util.ArrayList;
//import java.util.UUID;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//
//@SuppressWarnings("UnusedReturnValue")
//public final class GameActionsHelper extends GameActionsHelperBase
//{
//    public static ApplyPowerAction ApplyBurning(AbstractCreature source, AbstractCreature target, int amount)
//    {
//        return StackPower(source, new BurningPower(target, source, amount));
//    }
//
//    public static ApplyPowerAction ApplyPoison(AbstractCreature source, AbstractCreature target, int amount)
//    {
//        return StackPower(source, new PoisonPower(target, source, amount));
//    }
//
//    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
//    {
//        return AddToDefault(new ApplyPowerAction(target, source, power));
//    }
//
//    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
//    {
//        return AddToDefault(new ApplyPowerAction(target, source, power, stacks));
//    }
//
//    public static ApplyPowerSilently ApplyPowerSilently(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
//    {
//        return AddToDefault(new ApplyPowerSilently(target, source, power, stacks));
//    }
//
//    public static ApplyPowerAction ApplyVulnerable(AbstractCreature source, AbstractCreature target, int amount)
//    {
//        return StackPower(source, new VulnerablePower(target, amount, !source.isPlayer));
//    }
//
//    public static ApplyPowerAction ApplyWeak(AbstractCreature source, AbstractCreature target, int amount)
//    {
//        return StackPower(source, new WeakPower(target, amount, !source.isPlayer));
//    }
//
//    public static CallbackAction Callback(AbstractGameAction action, Object state, BiConsumer<Object, AbstractGameAction> onCompletion)
//    {
//        return AddToDefault(new CallbackAction(action, state, onCompletion));
//    }
//
//    public static CallbackAction Callback(AbstractGameAction action, Consumer<AbstractGameAction> onCompletion)
//    {
//        return AddToDefault(new CallbackAction(action, onCompletion));
//    }
//
//    public static CallbackAction Callback(AbstractGameAction action)
//    {
//        return AddToDefault(new CallbackAction(action));
//    }
//
//    public static CallbackAction Callback(Consumer<AbstractGameAction> onCompletion)
//    {
//        return Callback(new WaitAction(0.05f), onCompletion);
//    }
//
//    public static CallbackAction Callback(Object state, BiConsumer<Object, AbstractGameAction> onCompletion)
//    {
//        return Callback(new WaitAction(0.05f), state, onCompletion);
//    }
//
//    public static ChannelAction ChannelOrb(AbstractOrb orb, boolean autoEvoke)
//    {
//        return AddToDefault(new ChannelAction(orb, autoEvoke));
//    }
//
//    public static ChannelAction ChannelRandomOrb(boolean autoEvoke)
//    {
//        return AddToDefault(new ChannelAction(GameUtilities.GetRandomOrb(), autoEvoke));
//    }
//
//    public static CreateThrowingKnives CreateThrowingKnives(int amount)
//    {
//        return AddToDefault(new CreateThrowingKnives(amount));
//    }
//
//    public static CycleCards Cycle(int amount, String sourceName)
//    {
//        return AddToDefault(new CycleCards(sourceName, amount, false));
//    }
//
//    public static DealDamage DealDamage(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
//    {
//        return AddToDefault(new DealDamage(target, new DamageInfo(source, amount, damageType), effect));
//    }
//
//    public static DealDamage DealDamage(AbstractCard card, AbstractCreature target, AbstractGameAction.AttackEffect effect)
//    {
//        return AddToDefault(new DealDamage(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), effect));
//    }
//
//    public static DealDamageToAll DealDamageToAll(AbstractCard card, AbstractGameAction.AttackEffect effect)
//    {
//        return AddToDefault(new DealDamageToAll(AbstractDungeon.player, card.multiDamage, card.damageTypeForTurn, effect));
//    }
//
//    public static DealDamageToAll DealDamageToAll(int[] damageMatrix, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
//    {
//        return AddToDefault(new DealDamageToAll(AbstractDungeon.player, damageMatrix, damageType, effect));
//    }
//
//    public static DealDamageToRandomEnemy DealDamageToRandomEnemy(AbstractCard card, AbstractGameAction.AttackEffect effect)
//    {
//        return AddToDefault(new DealDamageToRandomEnemy(new DamageInfo(AbstractDungeon.player, card.baseDamage, card.damageTypeForTurn), effect));
//    }
//
//    public static DiscardFromHand DiscardFromHand(String sourceName, int amount, boolean isRandom)
//    {
//        return AddToDefault(new DiscardFromHand(sourceName, amount, isRandom));
//    }
//
//    public static DiscardFromPile DiscardFromPile(String sourceName, int amount, CardGroup... groups)
//    {
//        return AddToDefault(new DiscardFromPile(sourceName, amount, groups));
//    }
//
//    public static DrawCards Draw(int amount)
//    {
//        return AddToDefault(new DrawCards(amount));
//    }
//
//    public static ExhaustFromHand ExhaustFromHand(String sourceName, int amount, boolean isRandom)
//    {
//        return AddToDefault(new ExhaustFromHand(sourceName, amount, isRandom));
//    }
//
//    public static ExhaustFromPile ExhaustFromPile(String sourceName, int amount, CardGroup... groups)
//    {
//        return AddToDefault(new ExhaustFromPile(sourceName, amount, groups));
//    }
//
//    public static FetchFromPile FetchFromPile(String sourceName, int amount, CardGroup... groups)
//    {
//        return AddToDefault(new FetchFromPile(sourceName, amount, groups));
//    }
//
//    public static ApplyPowerAction GainAgility(int amount)
//    {
//        return StackPower(new AgilityPower(AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainArtifact(int amount)
//    {
//        return StackPower(new ArtifactPower(AbstractDungeon.player, amount));
//    }
//
//    public static GainBlock GainBlock(int amount)
//    {
//        return AddToDefault(new GainBlock(AbstractDungeon.player, AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainBlur(int amount)
//    {
//        return StackPower(new BlurPower(AbstractDungeon.player, amount));
//    }
//
//    public static GainEnergyAction GainEnergy(int amount)
//    {
//        return AddToDefault(new GainEnergyAction(amount));
//    }
//
//    public static ApplyPowerAction GainForce(int amount)
//    {
//        return StackPower(new ForcePower(AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainIntellect(int amount)
//    {
//        return StackPower(new IntellectPower(AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainMetallicize(int amount)
//    {
//        return StackPower(new MetallicizePower(AbstractDungeon.player, amount));
//    }
//
//    public static IncreaseMaxOrbAction GainOrbSlots(int slots)
//    {
//        return AddToDefault(new IncreaseMaxOrbAction(slots));
//    }
//
//    public static ApplyPowerAction GainPlatedArmor(int amount)
//    {
//        return StackPower(new PlatedArmorPower(AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainRandomStat(int amount)
//    {
//        int roll = AbstractDungeon.cardRandomRng.random(2);
//        switch (roll)
//        {
//            case 0:
//            {
//                return GainIntellect(amount);
//            }
//            case 1:
//            {
//                return GainAgility(amount);
//            }
//            case 2:
//            default:
//            {
//                return GainForce(amount);
//            }
//        }
//    }
//
//    public static ApplyPowerAction GainTemporaryArtifact(int amount)
//    {
//        return TemporaryArtifactPower.Apply(AbstractDungeon.player, AbstractDungeon.player, amount);
//    }
//
//    public static AddTemporaryHPAction GainTemporaryHP(int amount)
//    {
//        return AddToDefault(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainTemporaryThorns(int amount)
//    {
//        return StackPower(new EarthenThornsPower(AbstractDungeon.player, amount));
//    }
//
//    public static ApplyPowerAction GainThorns(int amount)
//    {
//        return StackPower(new ThornsPower(AbstractDungeon.player, amount));
//    }
//
//    public static HealAction Heal(int amount)
//    {
//        return AddToDefault(new HealAction(AbstractDungeon.player, AbstractDungeon.player, amount));
//    }
//
//    public static LoseHPAction LoseHP(int amount, AbstractGameAction.AttackEffect effect)
//    {
//        return AddToDefault(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, amount, effect));
//    }
//
//    public static MakeTempCard MakeTempCard(AbstractCard card, CardGroup group)
//    {
//        return AddToDefault(new MakeTempCard(card, group));
//    }
//
//    public static MakeTempCard MakeTempCardInDiscardPile(AbstractCard card, boolean upgraded, boolean makeCopy)
//    {
//        return AddToDefault(new MakeTempCard(card, AbstractDungeon.player.discardPile));
//    }
//
//    public static MakeTempCard MakeTempCardInDrawPile(AbstractCard card, boolean upgraded, boolean makeCopy)
//    {
//        return AddToDefault(new MakeTempCard(card, AbstractDungeon.player.drawPile));
//    }
//
//    public static MakeTempCard MakeTempCardInHand(AbstractCard card, boolean upgraded, boolean makeCopy)
//    {
//        return AddToDefault(new MakeTempCard(card, AbstractDungeon.player.hand));
//    }
//
//    public static ModifyAllCombatInstances ModifyAllCombatInstances(UUID uuid, Object state, BiConsumer<Object, AbstractCard> onCompletion)
//    {
//        return AddToDefault(new ModifyAllCombatInstances(uuid, state, onCompletion));
//    }
//
//    public static ModifyAllCombatInstances ModifyAllCombatInstances(UUID uuid, Consumer<AbstractCard> onCompletion)
//    {
//        return AddToDefault(new ModifyAllCombatInstances(uuid, onCompletion));
//    }
//
//    public static ModifyAllCombatInstances ModifyAllCombatInstances(UUID uuid)
//    {
//        return AddToDefault(new ModifyAllCombatInstances(uuid));
//    }
//
//    public static MotivateAction Motivate()
//    {
//        return AddToDefault(new MotivateAction(1));
//    }
//
//    public static ArrayList<MotivateAction> Motivate(int times)
//    {
//        ArrayList<MotivateAction> actions = new ArrayList<>();
//
//        for (int i = 0; i < times; i++)
//        {
//            actions.add(Motivate());
//        }
//
//        return actions;
//    }
//
//    public static MoveCard MoveCard(AbstractCard card, CardGroup destination, CardGroup source, boolean showEffect)
//    {
//        return AddToDefault(new MoveCard(card, destination, source, showEffect));
//    }
//
//    public static MoveCards MoveCards(CardGroup destination, CardGroup source)
//    {
//        return AddToDefault(new MoveCards(destination, source));
//    }
//
//    public static MoveCards MoveCards(CardGroup destination, CardGroup source, int amount)
//    {
//        return AddToDefault(new MoveCards(destination, source, amount, true, true));
//    }
//
//    public static ReducePowerAction ReducePower(AbstractCreature source, String powerID, int amount)
//    {
//        return AddToDefault(new ReducePowerAction(source, source, powerID, amount));
//    }
//
//    public static ReducePowerAction ReducePower(AbstractPower power, int amount)
//    {
//        return AddToDefault(new ReducePowerAction(power.owner, power.owner, power, amount));
//    }
//
//    public static RemoveSpecificPowerAction RemovePower(AbstractCreature source, AbstractPower power)
//    {
//        return AddToDefault(new RemoveSpecificPowerAction(source, source, power));
//    }
//
//    public static RemoveSpecificPowerAction RemovePower(AbstractCreature source, String powerID)
//    {
//        return AddToDefault(new RemoveSpecificPowerAction(source, source, powerID));
//    }
//
//    public static SFXAction SFX(String key)
//    {
//        return AddToDefault(new SFXAction(key));
//    }
//
//    public static SFXAction SFX(String key, float pitchVar)
//    {
//        return AddToDefault(new SFXAction(key, pitchVar));
//    }
//
//    public static SelectFromHand SelectFromHand(String sourceName, int amount, boolean isRandom)
//    {
//        return AddToDefault(new SelectFromHand(sourceName, amount, isRandom));
//    }
//
//    public static SelectFromPile SelectFromPile(String sourceName, int amount, CardGroup... groups)
//    {
//        return AddToDefault(new SelectFromPile(sourceName, amount, groups));
//    }
//
//    public static SequentialAction Sequential(AbstractGameAction action, AbstractGameAction action2)
//    {
//        return AddToDefault(new SequentialAction(action, action2));
//    }
//
//    public static ApplyPowerAction StackPower(AbstractCreature source, AbstractPower power)
//    {
//        return AddToDefault(new ApplyPowerAction(power.owner, source, power, power.amount));
//    }
//
//    public static ApplyPowerAction StackPower(AbstractPower power)
//    {
//        return StackPower(power.owner, power);
//    }
//
//    public static ReduceStrength ReduceStrength(AbstractCreature target, int amount, boolean temporary)
//    {
//        return AddToDefault(new ReduceStrength(AbstractDungeon.player, target, amount, temporary));
//    }
//
//    public static VFXAction VFX(AbstractGameEffect effect)
//    {
//        return AddToDefault(new VFXAction(effect));
//    }
//
//    public static VFXAction VFX(AbstractGameEffect effect, float duration)
//    {
//        return AddToDefault(new VFXAction(effect, duration));
//    }
//
//    public static WaitAction Wait(float duration)
//    {
//        return AddToDefault(new WaitAction(duration));
//    }
//
//    public static WaitRealtimeAction WaitRealtime(float duration)
//    {
//        return AddToDefault(new WaitRealtimeAction(duration));
//    }
//}
