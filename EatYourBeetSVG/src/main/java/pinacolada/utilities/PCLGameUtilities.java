package pinacolada.utilities;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.WeightedList;
import pinacolada.blights.common.UpgradedHand;
import pinacolada.cards.base.*;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.orbs.pcl.*;
import pinacolada.patches.cardLibrary.PCLCardLibraryPatches;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.resources.GR;
import pinacolada.stances.PCLStance;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static pinacolada.resources.GR.Enums.CardTags.*;

public class PCLGameUtilities extends GameUtilities
{
    private static final ArrayList<PCLPowerHelper> commonBuffs = new ArrayList<>();
    private static final ArrayList<PCLPowerHelper> commonDebuffs = new ArrayList<>();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static void AddAffinity(PCLAffinity affinity, int amount) {
        AddAffinity(affinity, amount, true);
    }

    public static void AddAffinity(PCLAffinity affinity, int amount, boolean showEffect) {
        PCLCombatStats.MatchingSystem.AddAffinity(affinity, amount);
        if (showEffect) {
            PCLCombatStats.MatchingSystem.Flash(affinity);
        }
    }

    public static void AddAffinities(PCLCardAffinities affinities, boolean showEffect) {
        PCLCombatStats.MatchingSystem.AddAffinities(affinities);
        if (showEffect) {
            for (PCLAffinity affinity : affinities.GetAffinities()) {
                PCLCombatStats.MatchingSystem.Flash(affinity);
            }
        }
    }

    public static void SpendAffinity(PCLAffinity affinity, int amount, boolean showEffect) {
        PCLCombatStats.MatchingSystem.SpendAffinity(affinity, amount);
        if (showEffect) {
            PCLCombatStats.MatchingSystem.Flash(affinity);
        }
    }

    public static boolean TrySpendAffinity(PCLAffinity affinity, int amount) {
        return TrySpendAffinity(affinity, amount, true);
    }

    public static boolean TrySpendAffinity(PCLAffinity affinity, int amount, boolean showEffect) {
        if (affinity == null) {
            PCLJUtils.LogWarning(null, "Tried to spend null affinity.");
            return false;
        }
        int requiredAmount = PCLCombatStats.OnTrySpendAffinity(affinity, amount, true);
        int baseAmount = PCLCombatStats.MatchingSystem.GetAffinityLevel(affinity, false);
        if (baseAmount >= requiredAmount) {
            SpendAffinity(affinity, requiredAmount, showEffect);
            return true;
        }
        return false;
    }

    public static AbstractPCLAffinityPower AddAffinityPowerLevel(PCLAffinity affinity, int amount)
    {
        final AbstractPCLAffinityPower power = PCLCombatStats.MatchingSystem.GetPower(affinity);
        if (power != null)
        {
            power.AddLevel(amount);
        }

        return power;
    }

    public static AbstractPCLAffinityPower AddAffinityPowerUse(PCLAffinity affinity, int amount)
    {
        final AbstractPCLAffinityPower power = PCLCombatStats.MatchingSystem.GetPower(affinity);
        if (power != null)
        {
            power.AddUse(amount);
        }

        return power;
    }

    public static void AddAffinityRerolls(int amount) {
        if (PCLCombatStats.MatchingSystem.AffinityMeter.Reroll != null) {
            PCLCombatStats.MatchingSystem.AffinityMeter.Reroll.triggerCondition.uses += amount;
            PCLCombatStats.MatchingSystem.AffinityMeter.Reroll.triggerCondition.Refresh(false);
        }
    }

    public static void AddBaseAffinityRerolls(int amount) {
        if (PCLCombatStats.MatchingSystem.AffinityMeter.Reroll != null) {
            PCLCombatStats.MatchingSystem.AffinityMeter.Reroll.triggerCondition.AddUses(amount);
        }
    }

    public static void SetCanChooseAffinityReroll(boolean value) {
        if (PCLCombatStats.MatchingSystem.AffinityMeter.Reroll != null) {
            PCLCombatStats.MatchingSystem.AffinityMeter.Reroll.SetCanChoose(value);
        }
    }

    public static boolean CanOrbApplyFocus(AbstractOrb orb) {
        return (!Plasma.ORB_ID.equals(orb.ID) && !Chaos.ORB_ID.equals(orb.ID));
    }

    public static boolean CanOrbApplyFocusToEvoke(AbstractOrb orb) {
        return (!Dark.ORB_ID.equals(orb.ID) && !Water.ORB_ID.equals(orb.ID) && !Earth.ORB_ID.equals(orb.ID));
    }

    public static boolean CanPlayTwice(AbstractCard card)
    {
        return !card.isInAutoplay && (!card.purgeOnUse || card.hasTag(GR.Enums.CardTags.PURGE));
    }

    public static boolean CanRemoveFromDeck(AbstractCard card)
    {
        return (card.rarity != AbstractCard.CardRarity.SPECIAL) && !SoulboundField.soulbound.get(card);
    }

    public static boolean CanShowUpgrades(boolean isLibrary)
    {
        return SingleCardViewPopup.isViewingUpgrade && (player == null || isLibrary
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD
                || AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN);
    }

    public static boolean CanSpendAffinityPower(PCLAffinity affinity)
    {
        return CanSpendAffinityPower(affinity, -1);
    }
    public static boolean CanSpendAffinityPower(PCLAffinity affinity, int amount)
    {
        AbstractPCLAffinityPower po = PCLCombatStats.MatchingSystem.GetPower(affinity);
        return po != null && po.CanSpend(amount >= 0 ? amount : po.triggerCondition.requiredAmount);
    }

    public static boolean TrySpendAffinityPower(PCLAffinity affinity)
    {
        return TrySpendAffinityPower(affinity, -1);
    }

    public static boolean TrySpendAffinityPower(PCLAffinity affinity, int amount)
    {
        AbstractPCLAffinityPower po = PCLCombatStats.MatchingSystem.GetPower(affinity);
        return po != null && po.TrySpend(amount >= 0 ? amount : po.triggerCondition.requiredAmount);
    }

    public static PCLAffinity ConvertEYBToPCLAffinity(Affinity affinity)
    {
        switch (affinity) {
            case Red:
                return PCLAffinity.Red;
            case Green:
                return PCLAffinity.Green;
            case Blue:
                return PCLAffinity.Blue;
            case Light:
                return PCLAffinity.Light;
            case Dark:
                return PCLAffinity.Dark;
            case Star:
                return PCLAffinity.Star;
        }
        return PCLAffinity.General;
    }

    public static Affinity ConvertPCLToEYBAffinity(PCLAffinity affinity)
    {
        switch (affinity) {
            case Red:
            case Orange:
                return Affinity.Red;
            case Green:
                return Affinity.Green;
            case Blue:
                return Affinity.Blue;
            case Light:
                return Affinity.Light;
            case Dark:
                return Affinity.Dark;
            case Silver:
            case Star:
                return Affinity.Star;
        }
        return Affinity.General;
    }

    public static void DecreaseBlock(AbstractCard card, int amount, boolean temporary)
    {
        ModifyBlock(card, Math.max(0, card.baseBlock - amount), temporary);
    }

    public static void DecreaseDamage(AbstractCard card, int amount, boolean temporary)
    {
        ModifyDamage(card, Math.max(0, card.baseDamage - amount), temporary);
    }

    public static void DecreaseMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        ModifyMagicNumber(card, Math.max(0, card.baseMagicNumber - amount), temporary);
    }

    public static void DecreaseSecondaryValue(PCLCard card, int amount, boolean temporary)
    {
        ModifySecondaryValue(card, Math.max(0, card.baseSecondaryValue - amount), temporary);
    }

    public static int GetCurrentMatchCombo()
    {
        return PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentMatchCombo();
    }

    public static AbstractOrb GetFirstOrb(String orbID)
    {
        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && (orbID == null || orbID.equals(orb.ID)))
            {
                return orb;
            }
        }

        return null;
    }

    public static int GetGold()
    {
        return player != null ? player.gold : 0;
    }

    public static PCLCardAffinities GetPCLAffinities(AbstractCard card)
    {
        return card instanceof PCLCard ? ((PCLCard) card).affinities : card instanceof EYBCard ? new PCLCardAffinities(null, ((EYBCard) card).affinities) : null;
    }

    public static PCLCardAffinity GetPCLAffinity(AbstractCard card, PCLAffinity affinity)
    {
        final PCLCardAffinities a = GetPCLAffinities(card);
        return a != null ? a.Get(affinity, false) : null;
    }

    public static int GetPCLAffinityLevel(AbstractCard card, PCLAffinity affinity, boolean useStarLevel)
    {
        final PCLCardAffinities a = GetPCLAffinities(card);
        return a != null ? a.GetLevel(affinity, useStarLevel) : 0;
    }

    public static int GetPCLAffinityPowerLevel(PCLAffinity affinity)
    {
        AbstractPCLAffinityPower po = PCLCombatStats.MatchingSystem.GetPower(affinity);
        return po != null ? po.GetEffectiveLevel() : 0;
    }

    public static int GetPCLAffinityScaling(AbstractCard card, PCLAffinity affinity, boolean useStarScaling)
    {
        final PCLCardAffinities a = GetPCLAffinities(card);
        return a != null ? a.GetScaling(affinity, useStarScaling) : 0;
    }

    public static PCLCardData GetPCLCardReplacement(String cardID) {
        return PCLCardLibraryPatches.GetReplacement(cardID);
    }

    public static PCLCard GetPCLCardReplacement(String cardID, boolean upgraded) {
        PCLCardData data = GetPCLCardReplacement(cardID);
        return data == null ? null : data.CreateNewInstance(upgraded);
    }

    public static AbstractCard GetCardReplacementOrDefault(String cardID, boolean upgraded) {
        PCLCardData data = GetPCLCardReplacement(cardID);
        return (AbstractCard)(data == null ? CardLibrary.getCopy(cardID, upgraded ? 1 : 0, 0) : data.CreateNewInstance(upgraded));
    }

    public static boolean HasRedAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Red, true) > 0;
    }

    public static boolean HasGreenAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Green, true) > 0;
    }

    public static boolean HasBlueAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Blue, true) > 0;
    }

    public static boolean HasOrangeAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Orange, true) > 0;
    }

    public static boolean HasLightAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Light, true) > 0;
    }

    public static boolean HasDarkAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Dark, true) > 0;
    }

    public static boolean HasMulticolorAffinity(AbstractCard card)
    {
        return GetPCLAffinityLevel(card, PCLAffinity.Star, true) > 0;
    }

    public static int GetDebuffsCount(AbstractCreature creature)
    {
        return (creature == null || creature.powers == null) ? 0 : GetDebuffsCount(creature.powers);
    }

    public static int GetDebuffsCount(ArrayList<AbstractPower> powers)
    {
        int result = 0;
        for (AbstractPower power : powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                result += 1;
            }
        }

        return result;
    }

    public static int GetDebuffsStacks(AbstractCreature creature)
    {
        return (creature == null || creature.powers == null) ? 0 : GetDebuffsStacks(creature.powers);
    }

    public static int GetDebuffsStacks(ArrayList<AbstractPower> powers)
    {
        int result = 0;
        for (AbstractPower power : powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                result += power.amount;
            }
        }

        return result;
    }

    public static int GetOrbBaseEvokeAmount(AbstractOrb orb) {
        Object f = GetOrbField(orb, "baseEvokeAmount");
        return (f != null ? (int) f : 0);
    }

    public static int GetOrbBasePassiveAmount(AbstractOrb orb) {
        Object f = GetOrbField(orb, "basePassiveAmount");
        return (f != null ? (int) f : 0);
    }

    public static Object GetOrbField(AbstractOrb orb, String field) {
        try {
            Field f = AbstractOrb.class.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(orb);
        }
        catch (NoSuchFieldException | IllegalAccessException var2) {
            PCLJUtils.LogWarning(orb, "Orb could not be modified");
            return null;
        }
    }

    public static ArrayList<PCLPowerHelper> GetPCLCommonBuffs()
    {
        if (commonBuffs.isEmpty())
        {
            for (PCLPowerHelper ph : PCLPowerHelper.ALL.values()) {
                if (!ph.IsDebuff && !ph.EndTurnBehavior.equals(PCLPowerHelper.Behavior.Temporary)) {
                    commonBuffs.add(ph);
                }
            }
        }

        return commonBuffs;
    }

    public static ArrayList<PCLPowerHelper> GetPCLCommonDebuffs()
    {
        if (commonDebuffs.isEmpty())
        {
            for (PCLPowerHelper ph : PCLPowerHelper.ALL.values()) {
                if (ph.IsDebuff) {
                    commonDebuffs.add(ph);
                }
            }
        }

        return commonDebuffs;
    }

    public static PCLEnemyIntent GetPCLIntent(AbstractMonster enemy)
    {
        return PCLEnemyIntent.Get(enemy);
    }

    public static ArrayList<PCLEnemyIntent> GetPCLIntents()
    {
        final ArrayList<PCLEnemyIntent> intents = new ArrayList<>();
        for (AbstractMonster m : GetEnemies(true))
        {
            intents.add(GetPCLIntent(m));
        }

        return intents;
    }

    public static <T> T GetPower(AbstractCreature owner, Class<T> powerType)
    {
        if (owner != null && owner.powers != null) {
            for (AbstractPower power : owner.powers)
            {
                if (powerType.isInstance(power))
                {
                    return powerType.cast(power);
                }
            }
        }

        return null;
    }

    public static <T extends AbstractPower> T GetPower(AbstractCreature creature, String powerID)
    {
        if (creature != null && creature.powers != null) {
            for (AbstractPower p : creature.powers)
            {
                if (p != null && powerID.equals(p.ID))
                {
                    try
                    {
                        return (T) p;
                    }
                    catch (ClassCastException e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public static int GetPowerAmount(PCLAffinity affinity) {
        return PCLCombatStats.MatchingSystem.GetPowerAmount(affinity);
    }

    // TODO make dynamic replacements for non-PCL cards
    public static AbstractCard GetAnyColorCardFiltered(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean allowHealing)
    {
        // Exclude Animator and PCL cards from the prismatic shard pool when playing as the opposite class
        boolean isAnimator = PCLGameUtilities.IsPlayerClass(eatyourbeets.resources.GR.Animator.PlayerClass);
        boolean isPCL = PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass);
        RandomizedList<AbstractCard> cardPool = new RandomizedList<>();
        for (AbstractCard c : CardLibrary.cards.values()) {
            if ((Settings.treatEverythingAsUnlocked() || !UnlockTracker.isCardLocked(c.cardID)) &&
                    (allowHealing || PCLGameUtilities.IsObtainableInCombat(c)) &&
                    (rarity == null || c.rarity == rarity) &&
                    ((type == null && !PCLGameUtilities.IsHindrance(c)) || c.type == type) &&
                    !(isAnimator && c instanceof PCLCard) &&
                    !(isPCL && c instanceof AnimatorCard))
                        {
                            cardPool.Add(c);
                        }
        }
        return cardPool.Retrieve(GetRNG());
    }

    public static AbstractOrb GetRandomCommonOrb() {
        switch (MathUtils.random(0,3)) {
            case 0:
                return new Dark();
            case 1:
                return new Frost();
            case 2:
                return new Fire();
            default:
                return new Lightning();
        }
    }

    public static AbstractOrb GetRandomOrb()
    {
        if (orbs.Size() == 0)
        {
            orbs.Add(new Lightning(), 7);
            orbs.Add(new Frost(), 7);
            orbs.Add(new Fire(), 6);
            orbs.Add(new Dark(), 6);
            orbs.Add(new Earth(), 5);
            orbs.Add(new Air(), 4);
            orbs.Add(new Plasma(), 3);
            orbs.Add(new Water(), 3);
        }

        return orbs.Retrieve(GetRNG(), false).makeCopy();
    }

    public static <T extends AbstractRelic> T GetRelic(String relicID)
    {
        if (player == null)
        {
            return null;
        }

        for (AbstractRelic relic : player.relics)
        {
            if (relic != null && relicID.equals(relic.relicId))
            {
                try
                {
                    return (T) relic;
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }

    public static <T> T GetRelic(Class<T> relicType)
    {
        for (AbstractRelic relic : player.relics)
        {
            if (relicType.isInstance(relic))
            {
                return relicType.cast(relic);
            }
        }

        return null;
    }

    public static ArrayList<String> GetRelicPool(AbstractRelic.RelicTier tier)
    {
        switch (tier)
        {
            case COMMON: return AbstractDungeon.commonRelicPool;
            case UNCOMMON: return AbstractDungeon.uncommonRelicPool;
            case RARE: return AbstractDungeon.rareRelicPool;
            case BOSS: return AbstractDungeon.bossRelicPool;
            case SHOP: return AbstractDungeon.shopRelicPool;
            default: return null;
        }
    }

    public static int GetTempHP(AbstractCreature creature)
    {
        return creature != null ? TempHPField.tempHp.get(creature) : 0;
    }

    public static int GetTempHP()
    {
        return GetTempHP(player);
    }

    public static int GetTimesPlayedThisTurn(AbstractCard card)
    {
        int result = 0;
        for (AbstractCard c : actionManager.cardsPlayedThisTurn)
        {
            if (c.uuid.equals(card.uuid))
            {
                result += 1;
            }
        }

        return result;
    }

    public static int GetUniqueOrbsCount()
    {
        final HashSet<String> orbs = new HashSet<>();
        for (AbstractOrb orb : player.orbs)
        {
            if (IsValidOrb(orb))
            {
                orbs.add(orb.ID);
            }
        }

        return orbs.size();
    }

    public static int GetXCostEnergy(AbstractCard card)
    {
        int amount = EnergyPanel.getCurrentEnergy();

        if (card.energyOnUse != -1)
        {
            amount = card.energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
        }

        return amount;
    }

    public static boolean HasOrb(String orbID)
    {
        return GetOrbCount(orbID) > 0;
    }

    public static int GetOrbCount(String orbID)
    {
        int count = 0;
        if (player != null && player.orbs != null)
        {
            for (AbstractOrb orb : player.orbs)
            {
                if (orbID.equals(orb.ID))
                {
                    count += 1;
                }
            }
        }

        return count;
    }

    public static boolean HasCooldown(AbstractCard card)
    {
        return card instanceof PCLCard && ((PCLCard) card).cooldown != null;
    }

    public static boolean HasEncounteredEvent(String eventID)
    {
        return GR.PCL.Dungeon.GetMapData(eventID) != null;
    }

    public static boolean HasUpgradableAffinities(AbstractCard c)
    {
        final PCLCardAffinities a = PCLGameUtilities.GetPCLAffinities(c);
        if (a != null)
        {
            if (a.Star != null && a.Star.level == 1)
            {
                return true;
            }

            for (PCLCardAffinity affinity : a.List)
            {
                if (affinity.level == 1)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static void HighlightMatchingCards(PCLAffinity affinity) {
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            final PCLCard temp = PCLJUtils.SafeCast(c, PCLCard.class);
            if (temp == null || (temp.affinities.GetLevel(affinity) == 0))
            {
                c.transparency = 0.35f;
            }
        }
    }

    public static boolean InStance(PCLAffinity affinity)
    {
        return player != null && player.stance instanceof PCLStance && affinity.equals(((PCLStance) player.stance).affinity);
    }

    public static boolean InStance(String stanceID)
    {
        return player != null && player.stance != null && player.stance.ID.equals(stanceID);
    }

    public static void IncreaseBlock(AbstractCard card, int amount, boolean temporary)
    {
        ModifyBlock(card, card.baseBlock + amount, temporary);
    }

    public static void IncreaseDamage(AbstractCard card, int amount, boolean temporary)
    {
        ModifyDamage(card, card.baseDamage + amount, temporary);
    }

    public static void IncreaseHandSizePermanently(float cX, float cY)
    {
        for (AbstractBlight blight : player.blights)
        {
            if (blight instanceof UpgradedHand)
            {
                ((UpgradedHand)blight).addAmount(1);
                return;
            }
        }
        GetCurrentRoom(true).spawnBlightAndObtain(cX, cY, new UpgradedHand());
    }

    public static void IncreaseMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        ModifyMagicNumber(card, card.baseMagicNumber + amount, temporary);
    }

    public static void IncreaseSecondaryValue(PCLCard card, int amount, boolean temporary)
    {
        ModifySecondaryValue(card, card.baseSecondaryValue + amount, temporary);
    }

    public static void IncreaseHitCount(PCLCard card, int amount, boolean temporary)
    {
        ModifyHitCount(card, card.baseHitCount + amount, temporary);
    }

    public static boolean IsAttacking(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK);
    }

    public static boolean IsCommonOrb(AbstractOrb orb)
    {
        return IsValidOrb(orb) && (Fire.ORB_ID.equals(orb.ID) || Frost.ORB_ID.equals(orb.ID) || Lightning.ORB_ID.equals(orb.ID) || Dark.ORB_ID.equals(orb.ID));
    }

    public static boolean IsDebuffing(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.DEBUFF ||
                intent == AbstractMonster.Intent.DEFEND_DEBUFF || intent == AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public static boolean IsSoul(AbstractCard card)
    {
        return card instanceof PCLCard && ((PCLCard) card).cooldown != null && ((PCLCard) card).cooldown.cardConstructor != null;
    }

    public static boolean IsCommonBuff(AbstractPower power)
    {
        return PCLJUtils.Any(PCLGameUtilities.GetPCLCommonBuffs(), ph -> ph.ID.equals(power.ID));
    }

    public static boolean IsCommonDebuff(AbstractPower power)
    {
        return PCLJUtils.Any(PCLGameUtilities.GetPCLCommonDebuffs(), ph -> ph.ID.equals(power.ID));
    }

    public static boolean IsDeadOrEscaped(AbstractCreature target)
    {
        return target == null || target.isDeadOrEscaped() || target.currentHealth <= 0;
    }

    public static boolean IsSameSeries(AbstractCard card1, AbstractCard card2)
    {
        PCLCard c1 = PCLJUtils.SafeCast(card1, PCLCard.class);
        PCLCard c2 = PCLJUtils.SafeCast(card2, PCLCard.class);
        return c1 != null && c2 != null && c1.series != null && c1.series.equals(c2.series);
    }

    public static void ModifyBlock(AbstractCard card, int amount, boolean temporary)
    {
        card.block = Math.max(0, amount);
        if (!temporary)
        {
            card.baseBlock = card.block;
        }
        card.isBlockModified = (card.block != card.baseBlock);
    }

    public static int ModifyCardDrawPerTurn(int amount, int minimumCardDraw)
    {
        final int newAmount = player.gameHandSize + amount;
        if (newAmount < minimumCardDraw)
        {
            amount += (minimumCardDraw - newAmount);
        }

        player.gameHandSize += amount;
        return amount;
    }

    public static void ModifyCardTag(AbstractCard card, AbstractCard.CardTags tag, boolean value)
    {
        if (tag == null || card == null) {
            return;
        }
        PCLCard aCard = PCLJUtils.SafeCast(card, PCLCard.class);
        EYBCard eCard = PCLJUtils.SafeCast(card, EYBCard.class);
        if (aCard != null) {
            if (AUTOPLAY.equals(tag)) {
                aCard.SetAutoplay(value);
            }
            else if (AFTERLIFE.equals(tag)) {
                aCard.SetAfterlife(value);
            }
            else if (PCL_ETHEREAL.equals(tag)) {
                aCard.SetEthereal(value);
            }
            else if (PCL_EXHAUST.equals(tag)) {
                aCard.SetExhaust(value);
            }
            else if (PCL_INNATE.equals(tag)) {
                aCard.SetInnate(value);
            }
            else if (PCL_RETAIN.equals(tag)) {
                aCard.SetRetain(value);
            }
            else if (PCL_RETAIN_ONCE.equals(tag)) {
                aCard.SetRetainOnce(value);
            }
            else if (PCL_UNPLAYABLE.equals(tag)) {
                aCard.SetUnplayable(value);
            }
            else if (DELAYED.equals(tag)) {
                aCard.SetDelayed(value);
            }
            else if (HARMONIC.equals(tag)) {
                aCard.SetHarmonic(value);
            }
            else if (HASTE.equals(tag)) {
                aCard.SetHaste(value);
            }
            else if (HASTE_INFINITE.equals(tag)) {
                aCard.SetPermanentHaste(value);
            }
            else if (LOYAL.equals(tag)) {
                aCard.SetLoyal(value);
            }
            else if (PROTAGONIST.equals(tag)) {
                aCard.SetHarmonic(value);
            }
            else if (PURGE.equals(tag)) {
                aCard.SetPurge(value);
            }
            else if (value) {
                aCard.tags.add(tag);
            }
            else {
                aCard.tags.remove(tag);
            }
        }
        else if (eCard != null) {
            if (PCL_ETHEREAL.equals(tag)) {
                eCard.SetEthereal(value);
            }
            else if (PCL_EXHAUST.equals(tag)) {
                eCard.SetExhaust(value);
            }
            else if (PCL_INNATE.equals(tag)) {
                eCard.SetInnate(value);
            }
            else if (PCL_RETAIN.equals(tag)) {
                eCard.SetRetain(value);
            }
            else if (PCL_RETAIN_ONCE.equals(tag)) {
                eCard.SetRetainOnce(value);
            }
            else if (PCL_UNPLAYABLE.equals(tag)) {
                eCard.SetUnplayable(value);
            }
            else if (DELAYED.equals(tag)) {
                eCard.SetDelayed(value);
            }
            else if (HASTE.equals(tag)) {
                eCard.SetHaste(value);
            }
            else if (value) {
                eCard.tags.add(tag);
            }
            else {
                eCard.tags.remove(tag);
            }
        }
        else if (value) {
            card.tags.add(tag);
        }
        else {
            card.tags.remove(tag);
        }
    }

    public static void ModifyCostForCombat(AbstractCard card, int amount, boolean relative)
    {
        final int previousCost = card.cost;
        if (relative)
        {
            card.costForTurn = Math.max(0, card.costForTurn + amount);
            card.cost = Math.max(0, card.cost + amount);
        }
        else
        {
            card.costForTurn = amount + (card.costForTurn - card.cost);
            card.cost = amount;
        }

        if (card.cost != previousCost)
        {
            card.isCostModified = true;
        }
        PCLCombatStats.OnCostChanged(card, previousCost, card.cost);
    }

    public static void ModifyCostForTurn(AbstractCard card, int amount, boolean relative)
    {
        final int previousCost = card.costForTurn;
        card.costForTurn = relative ? Math.max(0, card.costForTurn + amount) : amount;
        card.isCostModifiedForTurn = (card.cost != card.costForTurn);
        PCLCombatStats.OnCostChanged(card, previousCost, card.costForTurn);
    }

    public static void ModifyDamage(AbstractCard card, int amount, boolean temporary)
    {
        card.damage = Math.max(0, amount);
        if (!temporary)
        {
            card.baseDamage = card.damage;
        }
        card.isDamageModified = (card.damage != card.baseDamage);
    }

    public static void ModifyMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        card.magicNumber = amount;
        if (!temporary)
        {
            card.baseMagicNumber = card.magicNumber;
        }
        card.isMagicNumberModified = (card.magicNumber != card.baseMagicNumber);
    }

    public static void ModifySecondaryValue(PCLCard card, int amount, boolean temporary)
    {
        card.secondaryValue = amount;
        if (!temporary)
        {
            card.baseSecondaryValue = card.secondaryValue;
        }
        card.isSecondaryValueModified = (card.secondaryValue != card.baseSecondaryValue);
    }

    public static void ModifyHitCount(PCLCard card, int amount, boolean temporary)
    {
        card.hitCount = amount;
        if (!temporary)
        {
            card.baseHitCount = card.hitCount;
        }
        card.isHitCountModified = (card.hitCount != card.baseHitCount);
    }

    public static void ModifyOrbBaseEvokeAmount(AbstractOrb orb, int amount, boolean isRelative, boolean canModifyNonFocusOrb) {
        if (canModifyNonFocusOrb || CanOrbApplyFocus(orb)) {
            ModifyOrbField(orb, "baseEvokeAmount", amount, isRelative);
        }

    }

    public static void ModifyOrbBasePassiveAmount(AbstractOrb orb, int amount, boolean isRelative, boolean canModifyNonFocusOrb) {
        if (canModifyNonFocusOrb || CanOrbApplyFocus(orb)) {
            ModifyOrbField(orb, "basePassiveAmount", amount, isRelative);
        }
    }

    public static void ModifyOrbFocus(AbstractOrb orb, int amount, boolean isRelative, boolean canModifyNonFocusOrb) {
        if (canModifyNonFocusOrb || CanOrbApplyFocus(orb)) {
            ModifyOrbField(orb, "baseEvokeAmount", amount, isRelative);
            ModifyOrbField(orb, "basePassiveAmount", amount, isRelative);
        }
    }

    public static void ModifyOrbField(AbstractOrb orb, String field, int amount, boolean isRelative) {
        try {
            Field f = AbstractOrb.class.getDeclaredField(field);
            f.setAccessible(true);
            f.set(orb, isRelative ? amount + (int) f.get(orb) : amount);
            orb.applyFocus();
            orb.updateDescription();
        }
        catch (Exception e) {
            PCLJUtils.LogWarning(orb, "Orb could not be modified: " + e.getLocalizedMessage());
        }
    }

    public static boolean Retain(AbstractCard card)
    {
        if (CanRetain(card))
        {
            if (card instanceof PCLCard) {
                ModifyCardTag(card, PCL_RETAIN_ONCE, true);
            }
            card.retain = true;
            return true;
        }

        return false;
    }

    public static AbstractPCLAffinityPower MaintainPower(PCLAffinity affinity)
    {
        final AbstractPCLAffinityPower power = PCLCombatStats.MatchingSystem.GetPower(affinity);
        if (power != null)
        {
            power.Maintain();
        }

        return power;
    }

    public static void MaintainAllPowers()
    {
        for (AbstractPCLAffinityPower p : PCLCombatStats.MatchingSystem.Powers)
        {
            p.Maintain();
        }
    }

    public static void SetCardTag(AbstractCard card, AbstractCard.CardTags tag, boolean value)
    {
        if (value)
        {
            if (!card.tags.contains(tag))
            {
                card.tags.add(tag);
            }
        }
        else
        {
            card.tags.remove(tag);
        }
    }

    public static void SetUnplayableThisTurn(AbstractCard card)
    {
        CombatStats.UnplayableCards().add(card.uuid);
        PCLCombatStats.OnTagChanged(card, PCL_UNPLAYABLE, true);
    }

    public static void UsePenNib()
    {
        if (player.hasPower(PenNibPower.POWER_ID))
        {
            PCLActions.Bottom.ReducePower(player, PenNibPower.POWER_ID, 1);

            final AbstractRelic relic = player.getRelic(PenNib.ID);
            if (relic != null)
            {
                relic.counter = 0;
                relic.flash();
                relic.stopPulse();
            }
        }
    }
}
