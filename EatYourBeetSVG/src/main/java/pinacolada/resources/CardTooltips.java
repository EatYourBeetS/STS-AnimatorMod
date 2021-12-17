package pinacolada.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.characters.FakeCharacter;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.EnergizedPower;
import pinacolada.powers.common.*;
import pinacolada.powers.replacement.AntiArtifactSlowPower;
import pinacolada.powers.special.*;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLColors;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CardTooltips
{
    protected static final HashMap<String, PCLCardTooltip> tooltipIDs = new HashMap<>();
    protected static final HashMap<String, PCLCardTooltip> tooltips = new HashMap<>();

    public PCLCardTooltip Block = FindByID("Block").ShowText(false);
    public PCLCardTooltip Channel = FindByID("Channel").ShowText(false);
    public PCLCardTooltip Damage = FindByID("~Damage").ShowText(false);
    public PCLCardTooltip Dazed = FindByID("Dazed").ShowText(false);
    public PCLCardTooltip Energy = FindByName("[E]").ShowText(false);
    public PCLCardTooltip Evoke = FindByID("Evoke").ShowText(false);
    public PCLCardTooltip Exhaust = FindByID("Exhaust").ShowText(false);
    public PCLCardTooltip Health = FindByID("Health").ShowText(false);
    public PCLCardTooltip Series = FindByID("Series").ShowText(false);
    public PCLCardTooltip Stance = FindByID("Stance").ShowText(false);
    public PCLCardTooltip Trigger = FindByID("Trigger").ShowText(false);
    public PCLCardTooltip Upgrade = FindByID("Upgrade").ShowText(false);
    public PCLCardTooltip Void = FindByID("Void").ShowText(false);
    public PCLCardTooltip Trophy = FindByID("Ascension Trophy").ShowText(false);

    public PCLCardTooltip Desecration = FindByID(PCLAffinity.Dark.PowerName);
    public PCLCardTooltip DesecrationScaling = FindByID(PCLAffinity.Dark.GetScalingTooltipID());
    public PCLCardTooltip DesecrationStance = FindByID(PCLAffinity.Dark.GetStanceTooltipID());
    public PCLCardTooltip Endurance = FindByID(PCLAffinity.Orange.PowerName);
    public PCLCardTooltip EnduranceScaling = FindByID(PCLAffinity.Orange.GetScalingTooltipID());
    public PCLCardTooltip EnduranceStance = FindByID(PCLAffinity.Orange.GetStanceTooltipID());
    public PCLCardTooltip Might = FindByID(PCLAffinity.Red.PowerName);
    public PCLCardTooltip MightScaling = FindByID(PCLAffinity.Red.GetScalingTooltipID());
    public PCLCardTooltip MightStance = FindByID(PCLAffinity.Red.GetStanceTooltipID());
    public PCLCardTooltip Invocation = FindByID(PCLAffinity.Light.PowerName);
    public PCLCardTooltip InvocationScaling = FindByID(PCLAffinity.Light.GetScalingTooltipID());
    public PCLCardTooltip InvocationStance = FindByID(PCLAffinity.Light.GetStanceTooltipID());
    public PCLCardTooltip Technic = FindByID(PCLAffinity.Silver.PowerName);
    public PCLCardTooltip TechnicScaling = FindByID(PCLAffinity.Silver.GetScalingTooltipID());
    public PCLCardTooltip TechnicStance = FindByID(PCLAffinity.Silver.GetStanceTooltipID());
    public PCLCardTooltip Velocity = FindByID(PCLAffinity.Green.PowerName);
    public PCLCardTooltip VelocityScaling = FindByID(PCLAffinity.Green.GetScalingTooltipID());
    public PCLCardTooltip VelocityStance = FindByID(PCLAffinity.Green.GetStanceTooltipID());
    public PCLCardTooltip Wisdom = FindByID(PCLAffinity.Blue.PowerName);
    public PCLCardTooltip WisdomScaling = FindByID(PCLAffinity.Blue.GetScalingTooltipID());
    public PCLCardTooltip WisdomStance = FindByID(PCLAffinity.Blue.GetStanceTooltipID());

    public PCLCardTooltip Afterlife = FindByID("Afterlife");
    public PCLCardTooltip Air = FindByID("Air");
    public PCLCardTooltip Artifact = FindByID("Artifact");
    public PCLCardTooltip Aura = FindByID("Aura");
    public PCLCardTooltip Autoplay = FindByID("Autoplay");
    public PCLCardTooltip Blinded = FindByID("Blinded");
    public PCLCardTooltip BlockScaling = FindByID("~Block Scaling");
    public PCLCardTooltip Blur = FindByID("Blur");
    public PCLCardTooltip BranchUpgrade = FindByID("~Branch Upgrade");
    public PCLCardTooltip Brutal = FindByID("~Brutal");
    public PCLCardTooltip Burning = FindByID("Burning");
    public PCLCardTooltip BurningWeapon = FindByID("Burning Weapon");
    public PCLCardTooltip Chaos = FindByID("Chaos");
    public PCLCardTooltip CommonBuff = FindByID("Common Buff");
    public PCLCardTooltip CommonDebuff = FindByID("Common Debuff");
    public PCLCardTooltip Constricted = FindByID("Constricted");
    public PCLCardTooltip CounterAttack = FindByID("Counter-Attack");
    public PCLCardTooltip Dark = FindByID("Dark");
    public PCLCardTooltip Delayed = FindByID("~Delayed");
    public PCLCardTooltip DelayedDamage = FindByID("Delayed Damage");
    public PCLCardTooltip DemonForm = FindByID("Demon Form");
    public PCLCardTooltip Desecrated = FindByID("Desecrated");
    public PCLCardTooltip Dexterity = FindByID("Dexterity");
    public PCLCardTooltip Earth = FindByID("Earth");
    public PCLCardTooltip Electrified = FindByID("Electrified");
    public PCLCardTooltip Elemental = FindByID("~Elemental");
    public PCLCardTooltip ElementalExposure = FindByID("Elemental Exposure");
    public PCLCardTooltip ElementalMastery = FindByID("Elemental Mastery");
    public PCLCardTooltip EnchantedArmor = FindByID("Enchanted Armor");
    public PCLCardTooltip Energized = FindByID("Energized");
    public PCLCardTooltip Envenom = FindByID("Envenom");
    public PCLCardTooltip Ethereal = FindByID("~Ethereal");
    public PCLCardTooltip Fire = FindByID("Fire");
    public PCLCardTooltip FlameBarrier = FindByID("Flame Barrier");
    public PCLCardTooltip Focus = FindByID("Focus");
    public PCLCardTooltip Frail = FindByID("Frail");
    public PCLCardTooltip Freezing = FindByID("Freezing");
    public PCLCardTooltip Frost = FindByID("Frost");
    public PCLCardTooltip Genesis = FindByID("Genesis");
    public PCLCardTooltip Harmonic = FindByID("Harmonic");
    public PCLCardTooltip Haste = FindByID("~Haste");
    public PCLCardTooltip HasteInfinite = FindByID("~HasteInfinite");
    public PCLCardTooltip HasteOnce = FindByID("~HasteOnce");
    public PCLCardTooltip Impaired = FindByID("Impaired");
    public PCLCardTooltip Innate = FindByID("~Innate");
    public PCLCardTooltip Inspiration = FindByID("Inspiration");
    public PCLCardTooltip Intangible = FindByID("Intangible");
    public PCLCardTooltip Lightning = FindByID("Lightning");
    public PCLCardTooltip Limited = FindByID("Limited");
    public PCLCardTooltip LockOn = FindByID("~Lock-On");
    public PCLCardTooltip Loyal = FindByID("Loyal");
    public PCLCardTooltip Malleable = FindByID("Malleable");
    public PCLCardTooltip Metallicize = FindByID("Metallicize");
    public PCLCardTooltip Multicolor = FindByID("Multicolor");
    public PCLCardTooltip MulticolorScaling = FindByID("Multicolor Scaling");
    public PCLCardTooltip Multiform = FindByID("~Multiform");
    public PCLCardTooltip NeutralStance = FindByID("Neutral Stance");
    public PCLCardTooltip NextTurnBlock = FindByID("Next Turn Block");
    public PCLCardTooltip NextTurnDraw = FindByID("Next Turn Draw");
    public PCLCardTooltip OrbCore = FindByID("~Orb Core");
    public PCLCardTooltip OrbSlot = FindByID("Orb Slot");
    public PCLCardTooltip Phasing = FindByID("Phasing");
    public PCLCardTooltip Piercing = FindByID("~Piercing");
    public PCLCardTooltip Plasma = FindByID("Plasma");
    public PCLCardTooltip PlatedArmor = FindByID("Plated Armor");
    public PCLCardTooltip Poison = FindByID("Poison");
    public PCLCardTooltip Purge = FindByID("Purge");
    public PCLCardTooltip Ranged = FindByID("~Ranged");
    public PCLCardTooltip Regeneration = FindByID("Regeneration");
    public PCLCardTooltip Rejuvenation = FindByID("Rejuvenation");
    public PCLCardTooltip Resistance = FindByID("Resistance");
    public PCLCardTooltip Retain = FindByID("~Retain");
    public PCLCardTooltip RetainInfinite = FindByID("~RetainInfinite");
    public PCLCardTooltip RetainOnce = FindByID("~RetainOnce");
    public PCLCardTooltip Ritual = FindByID("Ritual");
    public PCLCardTooltip SelfImmolation = FindByID("Self-Immolation");
    public PCLCardTooltip SemiLimited = FindByID("Semi-Limited");
    public PCLCardTooltip Shackles = FindByID("Shackles");
    public PCLCardTooltip Sorcery = FindByID("Sorcery");
    public PCLCardTooltip Slow = FindByID("Slow");
    public PCLCardTooltip Starter = FindByID("Starter");
    public PCLCardTooltip Strength = FindByID("Strength");
    public PCLCardTooltip SupportDamage = FindByID("Support Damage");
    public PCLCardTooltip Swirled = FindByID("Swirled");
    public PCLCardTooltip TempHP = FindByID("Temporary HP");
    public PCLCardTooltip TemporaryThorns = FindByID("Temporary Thorns");
    public PCLCardTooltip Thorns = FindByID("Thorns");
    public PCLCardTooltip Unplayable = FindByID("Unplayable");
    public PCLCardTooltip Unique = FindByID("~Unique");
    public PCLCardTooltip Vigor = FindByID("Vigor");
    public PCLCardTooltip Vitality = FindByID("Vitality");
    public PCLCardTooltip Vulnerable = FindByID("Vulnerable");
    public PCLCardTooltip Water = FindByID("Water");
    public PCLCardTooltip Weak = FindByID("Weak");

    // No Description
    public PCLCardTooltip Affinity_Red = new PCLCardTooltip("Red Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_Green = new PCLCardTooltip("Green Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_Blue = new PCLCardTooltip("Blue Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_Orange = new PCLCardTooltip("Orange Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_Light = new PCLCardTooltip("Light Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_Dark = new PCLCardTooltip("Dark Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_Silver = new PCLCardTooltip("Silver Affinity", null).ShowText(false);
    public PCLCardTooltip Affinity_General = FindByID("Affinity");
    public PCLCardTooltip Affinity_Power = FindByID("Affinity Power");
    public PCLCardTooltip Affinity_Token = FindByID("Affinity Token");
    public PCLCardTooltip RandomOrb = new PCLCardTooltip("Random Orb", null);
    public PCLCardTooltip ThrowingKnife = new PCLCardTooltip(pinacolada.cards.pcl.special.ThrowingKnife.DATA.Strings.NAME, null);
    public PCLCardTooltip GriefSeed = new PCLCardTooltip(Curse_GriefSeed.DATA.Strings.NAME, null);
    public PCLCardTooltip Gold = new PCLCardTooltip(RewardItem.TEXT[1].trim(), null);

    public static void RegisterID(String id, PCLCardTooltip tooltip)
    {
        tooltipIDs.put(id, tooltip);
        tooltip.id = id;
    }

    public static void RegisterName(String name, PCLCardTooltip tooltip)
    {
        tooltips.put(name, tooltip);
    }

    public static Set<Map.Entry<String, PCLCardTooltip>> GetEntries() {
        return tooltipIDs.entrySet();
    }

    public static PCLCardTooltip FindByName(String name)
    {
        return tooltips.get(name);
    }

    public static PCLCardTooltip FindByID(String id)
    {
        return tooltipIDs.get(id);
    }

    public static String FindName(PCLCardTooltip tooltip)
    {
        for (String key : tooltips.keySet())
        {
            if (tooltips.get(key) == tooltip)
            {
                return key;
            }
        }

        return null;
    }

    public CardTooltips()
    {
        RegisterID(PCLAffinity.Red.GetAffinitySymbol(), Affinity_Red);
        RegisterID(PCLAffinity.Green.GetAffinitySymbol(), Affinity_Green);
        RegisterID(PCLAffinity.Blue.GetAffinitySymbol(), Affinity_Blue);
        RegisterID(PCLAffinity.Orange.GetAffinitySymbol(), Affinity_Orange);
        RegisterID(PCLAffinity.Light.GetAffinitySymbol(), Affinity_Light);
        RegisterID(PCLAffinity.Dark.GetAffinitySymbol(), Affinity_Dark);
        RegisterID(PCLAffinity.Silver.GetAffinitySymbol(), Affinity_Silver);
        RegisterID(PCLAffinity.Star.GetAffinitySymbol(), Multicolor);
        RegisterID(PCLAffinity.General.GetAffinitySymbol(), Affinity_General);

        RegisterID("HP", Health);
        RegisterID("THP", TempHP);
        RegisterID("Random Orb", RandomOrb);
        RegisterID("Grief Seed", GriefSeed);
        RegisterID("T-Knife", ThrowingKnife);
        RegisterID("Gold", Gold);
    }

    public void InitializeIcons()
    {
        PCLImages.Badges badges = GR.PCL.Images.Badges;
        Afterlife.SetIcon(badges.Afterlife.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_AFTERLIFE);
        Autoplay.SetIcon(badges.Autoplay.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_AUTOPLAY);
        Delayed.SetIcon(badges.Delayed.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_DELAYED);
        Ethereal.SetIcon(badges.Ethereal.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_ETHEREAL);
        Exhaust.SetIcon(badges.Exhaust.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_EXHAUST);
        Harmonic.SetIcon(badges.Harmonic.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_HARMONIC);
        Haste.SetIcon(badges.Haste.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_HASTE);
        HasteInfinite.SetIcon(badges.Haste.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_HASTE);
        HasteOnce.SetIcon(badges.Haste.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_HASTE);
        Innate.SetIcon(badges.Innate.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_INNATE);
        Loyal.SetIcon(badges.Loyal.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_LOYAL);
        Purge.SetIcon(badges.Purge.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_PURGE);
        Retain.SetIcon(badges.Retain.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_RETAIN);
        RetainInfinite.SetIcon(badges.Retain.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_RETAIN);
        RetainOnce.SetIcon(badges.Retain.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_RETAIN);
        Unplayable.SetIcon(badges.Unplayable.Texture(), 6).SetBadgeBackground(PCLColors.COLOR_UNPLAYABLE);

        PCLImages.CardIcons icons = GR.PCL.Images.Icons;
        Ranged.SetIcon(icons.Ranged.Texture(), 6);
        Elemental.SetIcon(icons.Elemental.Texture(), 6);
        Piercing.SetIcon(icons.Piercing.Texture(), 6);
        Brutal.SetIcon(icons.Brutal.Texture(), 6);
        TempHP.SetIcon(icons.TempHP.Texture(), 6).SetIconSizeMulti(0.75f, 0.75f);
        Health.SetIcon(icons.HP.Texture(), 6).SetIconSizeMulti(0.75f, 0.75f);
        Block.SetIcon(icons.Block.Texture(), 10).SetIconSizeMulti(1f, 0.9f);
        BlockScaling.SetIcon(icons.BlockScaling.Texture(), 10).SetIconSizeMulti(1f, 0.9f);
        Damage.SetIcon(icons.Damage.Texture(), 10).SetIconSizeMulti(1f, 0.9f);
        BranchUpgrade.SetIcon(icons.BranchUpgrade.Texture(), 6);
        Unique.SetIcon(icons.Unique.Texture(), 6);
        Multiform.SetIcon(icons.Multiform.Texture(), 6);

        PCLImages.AffinityIcons affinities = GR.PCL.Images.Affinities;
        Affinity_Red.SetIcon(affinities.Red.Texture(), 8);
        Affinity_Green.SetIcon(affinities.Green.Texture(), 8);
        Affinity_Blue.SetIcon(affinities.Blue.Texture(), 8);
        Affinity_Orange.SetIcon(affinities.Orange.Texture(), 8);
        Affinity_Light.SetIcon(affinities.Light.Texture(), 8);
        Affinity_Dark.SetIcon(affinities.Dark.Texture(), 8);
        Affinity_Silver.SetIcon(affinities.Silver.Texture(), 8);
        Affinity_General.SetIcon(affinities.General.Texture(), 8);
        Multicolor.SetIcon(affinities.Star.Texture(), 8);
        MulticolorScaling.icon = Multicolor.icon;
        Affinity_Power.icon = Affinity_Token.icon = Affinity_General.icon;

        PCLImages.Tooltips tooltips = GR.PCL.Images.Tooltips;
        ThrowingKnife.SetIcon(tooltips.ThrowingKnife.Texture(), 6);
        GriefSeed.SetIcon(tooltips.GriefSeed.Texture(), 6);
        Lightning.SetIcon(tooltips.Lightning.Texture(), 6);
        Fire.SetIcon(tooltips.Fire.Texture(), 6);
        Plasma.SetIcon(tooltips.Plasma.Texture(), 6);
        Dark.SetIcon(tooltips.Dark.Texture(), 6);
        Frost.SetIcon(tooltips.Frost.Texture(), 6);
        Air.SetIcon(tooltips.Air.Texture(), 6);
        Earth.SetIcon(tooltips.Earth.Texture(), 6);
        Water.SetIcon(tooltips.Water.Texture(), 6);
        Chaos.SetIcon(tooltips.Chaos.Texture(), 6);
        RandomOrb.SetIcon(tooltips.RandomOrb.Texture(), 6);
        Gold.SetIcon(tooltips.Gold.Texture(), 6);
        OrbSlot.SetIcon(tooltips.OrbSlot.Texture(), 6);
        RandomOrb.SetIcon(tooltips.RandomOrb.Texture(), 6);
        Regeneration.SetIcon(tooltips.Regeneration.Texture(), 6);

        LoadFromPower(Burning, new BurningPower(FakeCharacter.Instance, null, 0));
        LoadFromPower(Blinded, new BlindedPower(FakeCharacter.Instance, null, 0));
        LoadFromPower(Electrified, new ElectrifiedPower(FakeCharacter.Instance, null,0));
        LoadFromPower(Freezing, new FreezingPower(FakeCharacter.Instance, null,0));
        LoadFromPower(Poison, new PoisonPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(1.05f, 1f);
        LoadFromPower(Artifact, new ArtifactPower(FakeCharacter.Instance, 0));
        LoadFromPower(Blur, new BlurPower(FakeCharacter.Instance, 0));
        LoadFromPower(Constricted, new ConstrictedPower(null, FakeCharacter.Instance, 0));
        LoadFromPower(CounterAttack, new CounterAttackPower(FakeCharacter.Instance, 0));
        LoadFromPower(DelayedDamage, new DelayedDamagePower(FakeCharacter.Instance, 0));
        LoadFromPower(DemonForm, new DemonFormPower(FakeCharacter.Instance, 0));
        LoadFromPower(Dexterity, new DexterityPower(FakeCharacter.Instance, 0));
        LoadFromPower(ElementalExposure, new ElementalExposurePower(FakeCharacter.Instance, 0));
        LoadFromPower(ElementalMastery, new ElementalMasteryPower(FakeCharacter.Instance, 0));
        LoadFromPower(EnchantedArmor, new EnchantedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Energized, new EnergizedPower(FakeCharacter.Instance, 0));
        LoadFromPower(Envenom, new EnvenomPower(FakeCharacter.Instance, 0));
        LoadFromPower(FlameBarrier, new FlameBarrierPower(FakeCharacter.Instance, 0));
        LoadFromPower(BurningWeapon, new BurningWeaponPower(FakeCharacter.Instance, 0));
        LoadFromPower(Focus, new FocusPower(FakeCharacter.Instance, 0));
        LoadFromPower(Genesis, new GenesisPower(FakeCharacter.Instance, 0));
        LoadFromPower(Impaired, new ImpairedPower(FakeCharacter.Instance, 0));
        LoadFromPower(Inspiration, new InspirationPower(FakeCharacter.Instance, 0));
        LoadFromPower(Intangible, new IntangiblePlayerPower(FakeCharacter.Instance, 0));
        LoadFromPower(LockOn, new LockOnPower(FakeCharacter.Instance, 0));
        LoadFromPower(Malleable, new MalleablePower(FakeCharacter.Instance, 0));
        LoadFromPower(Metallicize, new MetallicizePower(FakeCharacter.Instance, 0));
        LoadFromPower(Phasing, new PhasingPower(FakeCharacter.Instance, 0));
        LoadFromPower(PlatedArmor, new PlatedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Rejuvenation, new RejuvenationPower(FakeCharacter.Instance, 0));
        LoadFromPower(Resistance, new ResistancePower(FakeCharacter.Instance, 0));
        LoadFromPower(Ritual, new RitualPower(FakeCharacter.Instance, 0, true));
        LoadFromPower(SelfImmolation, new SelfImmolationPower(FakeCharacter.Instance, 0));
        LoadFromPower(Shackles, new GainStrengthPower(FakeCharacter.Instance, 0));
        LoadFromPower(Slow, new AntiArtifactSlowPower(FakeCharacter.Instance, 0));
        LoadFromPower(Sorcery, new SorceryPower(FakeCharacter.Instance, 0));
        LoadFromPower(Strength, new StrengthPower(FakeCharacter.Instance, 0));
        LoadFromPower(SupportDamage, new SupportDamagePower(FakeCharacter.Instance, 0));
        LoadFromPower(Swirled, new SwirledPower(FakeCharacter.Instance, 0));
        LoadFromPower(Desecrated, new DesecratedPower(FakeCharacter.Instance, 0));
        LoadFromPower(TemporaryThorns, new TemporaryThornsPower(FakeCharacter.Instance, 0));
        LoadFromPower(Thorns, new ThornsPower(FakeCharacter.Instance, 0));
        LoadFromPower(Vigor, new VigorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Vitality, new VitalityPower(FakeCharacter.Instance, 0));

        //These use AbstractDungeon.player
        LoadFromPower(Weak, new WeakPower(null, 0, false)).SetIconSizeMulti(1f, 0.9f);
        LoadFromPower(Vulnerable, new VulnerablePower(null, 0, false));
        LoadFromPower(Frail, new FrailPower(null, 0, false));

        Trophy.SetIcon(GR.PCL.Images.BRONZE_TROPHY.Texture());

        //Common Debuff/Buff require loading powers for existing tooltips
        CommonBuff.description = PCLJUtils.JoinStrings(", ", new HashSet<>(PCLJUtils.Map(PCLGameUtilities.GetPCLCommonBuffs(), ph -> "[" + ph.Tooltip.id + "]")));
        CommonDebuff.description = PCLJUtils.JoinStrings(", ", new HashSet<>(PCLJUtils.Map(PCLGameUtilities.GetPCLCommonDebuffs(), ph -> "[" + ph.Tooltip.id + "]")));
    }

    private PCLCardTooltip LoadFromPower(PCLCardTooltip tooltip, AbstractPower power)
    {
        PCLPower p = PCLJUtils.SafeCast(power, PCLPower.class);
        if (p == null)
        {
            tooltip.SetIcon(power.region48).SetIconSizeMulti(0.9f, 0.9f);
        }
        else if (p.powerIcon != null)
        {
            tooltip.SetIcon(p.powerIcon);
        }
        else if (p.img != null)
        {
            tooltip.SetIcon(p.img);
        }
        else {
            throw new RuntimeException("Image could not be read for " + p.name);
        }

        return tooltip;
    }

    private TextureRegion LoadFromBadge(TextureCache textureCache)
    {
        Texture texture = textureCache.Texture();
        int w = texture.getWidth();
        int h = texture.getHeight();
        return new TextureAtlas.AtlasRegion(texture, w / 6, h / 6, w - (w / 3), h - (h / 3));
    }

    private TextureRegion LoadFromLargeIcon(TextureCache textureCache)
    {
        Texture texture = textureCache.Texture();
        int w = texture.getWidth();
        int h = texture.getHeight();
        return new TextureAtlas.AtlasRegion(texture, w / 6, h / 6, w - (w / 3), h - (h / 3));
    }
}
