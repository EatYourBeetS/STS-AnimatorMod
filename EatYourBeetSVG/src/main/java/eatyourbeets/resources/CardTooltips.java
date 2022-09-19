package eatyourbeets.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.characters.FakeCharacter;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.powers.animator.EnchantedArmorPlayerPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.powers.animatorClassic.EnchantedArmorPower;
import eatyourbeets.powers.common.*;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.powers.unnamed.AmplificationPower;
import eatyourbeets.powers.unnamed.MarkedPower;
import eatyourbeets.powers.unnamed.SummoningSicknessPower;
import eatyourbeets.powers.unnamed.WitheringPower;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.resources.common.CommonResources;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.JUtils;

import java.util.Collection;
import java.util.HashMap;

public class CardTooltips
{
    protected static final HashMap<AbstractPlayer.PlayerClass, TooltipContainer> tooltips = new HashMap<>();
    protected static class TooltipContainer
    {
        protected final AbstractPlayer.PlayerClass playerClass;
        protected final HashMap<String, EYBCardTooltip> tooltipIDs = new HashMap<>();
        protected final HashMap<String, EYBCardTooltip> tooltipNames = new HashMap<>();

        protected EYBCardTooltip GetByID(String id)
        {
            return tooltipIDs.get(id);
        }

        protected EYBCardTooltip GetByName(String name)
        {
            return tooltipNames.get(name);
        }

        protected void AddByID(String id, EYBCardTooltip tooltip)
        {
            tooltipIDs.put(id, tooltip);
        }

        protected void AddByName(String name, EYBCardTooltip tooltip)
        {
            tooltipNames.put(name, tooltip);
        }

        protected TooltipContainer(AbstractPlayer.PlayerClass playerClass)
        {
            this.playerClass = playerClass;
        }
    }

    public EYBCardTooltip Energy = FindByName(null,"[E]").ShowText(false);
    public EYBCardTooltip Unplayable = FindByID(null, "Unplayable").ShowText(false);
    public EYBCardTooltip RetainOnce = FindByID(null, "~RetainOnce").ShowText(false);
    public EYBCardTooltip Exhaust = FindByID(null, "Exhaust").ShowText(false);
    public EYBCardTooltip Channel = FindByID(null, "Channel").ShowText(false);
    public EYBCardTooltip Evoke = FindByID(null, "Evoke").ShowText(false);
    public EYBCardTooltip Block = FindByID(null, "Block").ShowText(false);
    public EYBCardTooltip Health = FindByID(null, "Health").ShowText(false);
    public EYBCardTooltip Upgrade = FindByID(null, "Upgrade").ShowText(false);
    public EYBCardTooltip Stance = FindByID(null, "Stance").ShowText(false);
    //
    public EYBCardTooltip Wound = FindByID(null, "Wound").ShowText(false);
    public EYBCardTooltip Burn = FindByID(null, "Burn").ShowText(false);
    public EYBCardTooltip Void = FindByID(null, "Void").ShowText(false);
    public EYBCardTooltip Dazed = FindByID(null, "Dazed").ShowText(false);
    public EYBCardTooltip Curse = FindByID(null, "Curse").ShowText(false);
    public EYBCardTooltip Slimed = FindByID(null, "Slimed").ShowText(false);
    //
    public EYBCardTooltip Discard = FindByID(null, "Discard").ShowText(false).SetKeyword(false);
    public EYBCardTooltip Debuff = FindByID(null, "Debuff").ShowText(false);
    public EYBCardTooltip ZeroCost = FindByID(null, "Zero-Cost").ShowText(false);
    public EYBCardTooltip LowCost = FindByID(null, "Low-Cost");
    public EYBCardTooltip HighCost = FindByID(null, "High-Cost");
    public EYBCardTooltip Strength = FindByID(null, "Strength");
    public EYBCardTooltip Dexterity = FindByID(null, "Dexterity");
    public EYBCardTooltip Focus = FindByID(null, "Focus");

    public EYBCardTooltip Inspiration = FindByID(null, "Inspiration");
    public EYBCardTooltip SpecialAction = FindByID(null, "Special Action");
    public EYBCardTooltip Starter = FindByID(null, "Starter");
    public EYBCardTooltip Limited = FindByID(null, "Limited");
    public EYBCardTooltip SemiLimited = FindByID(null, "Semi-Limited");
    public EYBCardTooltip Unique = FindByID(null, "~Unique");
    public EYBCardTooltip Elemental = FindByID(null, "~Elemental");
    public EYBCardTooltip Piercing = FindByID(null, "~Piercing");
    public EYBCardTooltip Ranged = FindByID(null, "~Ranged");
    public EYBCardTooltip Damage = FindByID(null, "~Damage");
    public EYBCardTooltip Purge = FindByID(null, "Purge");
    public EYBCardTooltip Flight = FindByID(null, "Flight");
    public EYBCardTooltip DelayedDamage = FindByID(null, "Delayed Damage");
    public EYBCardTooltip NeutralStance = FindByID(null, "Neutral Stance");
    public EYBCardTooltip SupportDamage = FindByID(null, "Support Damage");
    public EYBCardTooltip OrbCore = FindByID(null, "~Orb Core");
    public EYBCardTooltip Innate = FindByID(null, "~Innate");
    public EYBCardTooltip Delayed = FindByID(null, "~Delayed");
    public EYBCardTooltip Ethereal = FindByID(null, "~Ethereal");
    public EYBCardTooltip Fading = FindByID(null, "~Fading");
    public EYBCardTooltip Haste = FindByID(null, "~Haste");
    public EYBCardTooltip Retain = FindByID(null, "~Retain");
    public EYBCardTooltip Metallicize = FindByID(null, "Metallicize");
    public EYBCardTooltip PlatedArmor = FindByID(null, "Plated Armor");
    public EYBCardTooltip CounterAttack = FindByID(null, "Counter-Attack");
    public EYBCardTooltip Vitality = FindByID(null, "Vitality");
    public EYBCardTooltip Invocation = FindByID(null, "Invocation");
    public EYBCardTooltip Envenom = FindByID(null, "Envenom");
    public EYBCardTooltip NoxiousFumes = FindByID(null, "Noxious Fumes");
    public EYBCardTooltip Vigor = FindByID(null, "Vigor");
    public EYBCardTooltip Ritual = FindByID(null, "Ritual");
    public EYBCardTooltip TempHP = FindByID(null, "Temporary HP");
    public EYBCardTooltip MultiHP = FindByID(null, "Multi HP");
    public EYBCardTooltip Weak = FindByID(null, "Weak");
    public EYBCardTooltip Vulnerable = FindByID(null, "Vulnerable");
    public EYBCardTooltip Frail = FindByID(null, "Frail");
    public EYBCardTooltip Poison = FindByID(null, "Poison");
    public EYBCardTooltip Burning = FindByID(null, "Burning");
    public EYBCardTooltip Freezing = FindByID(null, "Freezing");
    public EYBCardTooltip Thorns = FindByID(null, "Thorns");
    public EYBCardTooltip Constricted = FindByID(null, "Constricted");
    public EYBCardTooltip Malleable = FindByID(null, "Malleable");
    public EYBCardTooltip FlameBarrier = FindByID(null, "Flame Barrier");
    public EYBCardTooltip Blur = FindByID(null, "~Blur");
    public EYBCardTooltip Intangible = FindByID(null, "~Intangible");
    public EYBCardTooltip Artifact = FindByID(null, "Artifact");
    public EYBCardTooltip Shackles = FindByID(null, "Shackles");
    public EYBCardTooltip LockOn = FindByID(null, "~Lock-On");
    public EYBCardTooltip Lightning = FindByID(null, "Lightning");
    public EYBCardTooltip Plasma = FindByID(null, "Plasma");
    public EYBCardTooltip Frost = FindByID(null, "Frost");
    public EYBCardTooltip Fire = FindByID(null, "Fire");
    public EYBCardTooltip Chaos = FindByID(null, "Chaos");
    public EYBCardTooltip Earth = FindByID(null, "Earth");
    public EYBCardTooltip Aether = FindByID(null, "Aether");
    public EYBCardTooltip Dark = FindByID(null, "Dark");

    //Animator
    public EYBCardTooltip Boost_Affinity = FindByID(GR.Animator.PlayerClass, "Boost");
    public EYBCardTooltip EnchantedArmor = FindByID(GR.Animator.PlayerClass, "Enchanted Armor");
    public EYBCardTooltip Affinity_Star = FindByID(GR.Animator.PlayerClass, "Multicolor");
    public EYBCardTooltip Affinity_Sealed = FindByID(GR.Animator.PlayerClass, "Affinity Seal");
    public EYBCardTooltip Affinity_General = FindByID(GR.Animator.PlayerClass, "Affinity");
    public EYBCardTooltip Affinity_Power = FindByID(GR.Animator.PlayerClass, "Affinity Power");
    public EYBCardTooltip Affinity_Token = FindByID(GR.Animator.PlayerClass, "Affinity Token");
    public EYBCardTooltip RedPower = FindByID(GR.Animator.PlayerClass, "Force");
    public EYBCardTooltip GreenPower = FindByID(GR.Animator.PlayerClass, "Agility");
    public EYBCardTooltip BluePower = FindByID(GR.Animator.PlayerClass, "Intellect");
    public EYBCardTooltip LightPower = FindByID(GR.Animator.PlayerClass, "Blessing");
    public EYBCardTooltip DarkPower = FindByID(GR.Animator.PlayerClass, "Corruption");
    public EYBCardTooltip RedStance = FindByID(GR.Animator.PlayerClass, "Force Stance");
    public EYBCardTooltip GreenStance = FindByID(GR.Animator.PlayerClass, "Agility Stance");
    public EYBCardTooltip BlueStance = FindByID(GR.Animator.PlayerClass, "Intellect Stance");
    public EYBCardTooltip DarkStance = FindByID(GR.Animator.PlayerClass, "Corruption Stance");
    public EYBCardTooltip RedScaling = FindByID(GR.Animator.PlayerClass, "Red Scaling");
    public EYBCardTooltip GreenScaling = FindByID(GR.Animator.PlayerClass, "Green Scaling");
    public EYBCardTooltip BlueScaling = FindByID(GR.Animator.PlayerClass, "Blue Scaling");
    public EYBCardTooltip DarkScaling = FindByID(GR.Animator.PlayerClass, "Dark Scaling");
    public EYBCardTooltip LightScaling = FindByID(GR.Animator.PlayerClass, "Light Scaling");
    public EYBCardTooltip MulticolorScaling = FindByID(GR.Animator.PlayerClass, "Multicolor Scaling");

    //AnimatorClassic
    public EYBCardTooltip Boost_Synergy = FindByID(GR.AnimatorClassic.PlayerClass, "Boost");
    public EYBCardTooltip EnchantedArmor_Classic = FindByID(GR.AnimatorClassic.PlayerClass, "Enchanted Armor");
    public EYBCardTooltip Spellcaster = FindByID(GR.AnimatorClassic.PlayerClass, "Spellcaster");
    public EYBCardTooltip MartialArtist = FindByID(GR.AnimatorClassic.PlayerClass, "Martial Artist");
    public EYBCardTooltip Shapeshifter = FindByID(GR.AnimatorClassic.PlayerClass, "Shapeshifter");
    public EYBCardTooltip Force = FindByID(GR.AnimatorClassic.PlayerClass, "Force");
    public EYBCardTooltip Agility = FindByID(GR.AnimatorClassic.PlayerClass, "Agility");
    public EYBCardTooltip Intellect = FindByID(GR.AnimatorClassic.PlayerClass, "Intellect");
    public EYBCardTooltip Blessing = FindByID(GR.AnimatorClassic.PlayerClass, "Blessing");
    public EYBCardTooltip Corruption = FindByID(GR.AnimatorClassic.PlayerClass, "Corruption");
    public EYBCardTooltip AgilityScaling = FindByID(GR.AnimatorClassic.PlayerClass, "Agility Scaling");
    public EYBCardTooltip ForceScaling = FindByID(GR.AnimatorClassic.PlayerClass, "Force Scaling");
    public EYBCardTooltip IntellectScaling = FindByID(GR.AnimatorClassic.PlayerClass, "Intellect Scaling");
    public EYBCardTooltip CorruptionScaling = FindByID(GR.AnimatorClassic.PlayerClass, "Corruption Scaling");
    public EYBCardTooltip BlessingScaling = FindByID(GR.AnimatorClassic.PlayerClass, "Blessing Scaling");
    public EYBCardTooltip AgilityStance = FindByID(GR.AnimatorClassic.PlayerClass, "Agility Stance");
    public EYBCardTooltip ForceStance = FindByID(GR.AnimatorClassic.PlayerClass, "Force Stance");
    public EYBCardTooltip IntellectStance = FindByID(GR.AnimatorClassic.PlayerClass, "Intellect Stance");
    public EYBCardTooltip CorruptionStance = FindByID(GR.AnimatorClassic.PlayerClass, "Corruption Stance");

    // Unnamed
    public EYBCardTooltip Summon = FindByID(GR.Unnamed.PlayerClass, "Summon");
    public EYBCardTooltip Attachment = FindByID(GR.Unnamed.PlayerClass, "Attachment");
    public EYBCardTooltip Amplification = FindByID(GR.Unnamed.PlayerClass, "Amplification");
    public EYBCardTooltip Withering = FindByID(GR.Unnamed.PlayerClass, "Withering");
    public EYBCardTooltip SummoningSickness = FindByID(GR.Unnamed.PlayerClass, "Summoning Sickness");
    public EYBCardTooltip Mark = FindByID(GR.Unnamed.PlayerClass, "Mark");
    public EYBCardTooltip Recast = FindByID(GR.Unnamed.PlayerClass, "~Recast");

    // No Description
    public EYBCardTooltip Card = new EYBCardTooltip("Card", null).ShowText(false);
    public EYBCardTooltip Potion = new EYBCardTooltip("Potion", null).ShowText(false);
    public EYBCardTooltip Relic = new EYBCardTooltip("Relic", null).ShowText(false);
    public EYBCardTooltip Special = new EYBCardTooltip("Special", null).ShowText(false);
    public EYBCardTooltip Affinity_Red = new EYBCardTooltip("Red Affinity", null).ShowText(false);
    public EYBCardTooltip Affinity_Green = new EYBCardTooltip("Green Affinity", null).ShowText(false);
    public EYBCardTooltip Affinity_Blue = new EYBCardTooltip("Blue Affinity", null).ShowText(false);
    public EYBCardTooltip Affinity_Light = new EYBCardTooltip("Light Affinity", null).ShowText(false);
    public EYBCardTooltip Affinity_Dark = new EYBCardTooltip("Dark Affinity", null).ShowText(false);
    public EYBCardTooltip RandomOrb = new EYBCardTooltip("Random Orb", null);
    public EYBCardTooltip OrbSlot = new EYBCardTooltip("Orb Slot", null);
    public EYBCardTooltip ThrowingKnife = new EYBCardTooltip(eatyourbeets.cards.animator.special.ThrowingKnife.DATA.Strings.NAME, null);
    public EYBCardTooltip GriefSeed = new EYBCardTooltip(Curse_GriefSeed.DATA.Strings.NAME, null);
    public EYBCardTooltip Gold = new EYBCardTooltip(RewardItem.TEXT[1].trim(), null);

    public static TooltipContainer GetContainer(AbstractPlayer.PlayerClass playerClass)
    {
        return tooltips.computeIfAbsent(playerClass, TooltipContainer::new);
    }

    public static void RegisterID(AbstractPlayer.PlayerClass playerClass, String id, EYBCardTooltip tooltip)
    {
        GetContainer(playerClass).AddByID(id, tooltip);
        tooltip.id = id;
    }

    public static void RegisterName(AbstractPlayer.PlayerClass playerClass, String name, EYBCardTooltip tooltip)
    {
        GetContainer(playerClass).AddByName(name, tooltip);
    }

    public static Collection<EYBCardTooltip> GetAll(AbstractPlayer.PlayerClass playerClass)
    {
        return GetContainer(playerClass).tooltipIDs.values();
    }

    public static EYBCardTooltip FindByName(AbstractPlayer.PlayerClass playerClass, String name)
    {
        EYBCardTooltip tooltip = GetContainer(playerClass).GetByName(name);
        if (tooltip == null && playerClass != null)
        {
            tooltip = GetContainer(null).GetByName(name);
        }

        return tooltip;
    }

    public static EYBCardTooltip FindByID(AbstractPlayer.PlayerClass playerClass, String id)
    {
        EYBCardTooltip tooltip = GetContainer(playerClass).GetByID(id);
        if (tooltip == null && playerClass != null)
        {
            tooltip = GetContainer(null).GetByID(id);
        }

        return tooltip;
    }

    public CardTooltips()
    {
        RegisterID(null, "CARD", Card);
        RegisterID(null, "POTION", Potion);
        RegisterID(null, "RELIC", Relic);
        RegisterID(null, "SPECIAL", Special);

        RegisterID(GR.Animator.PlayerClass, "A_Red", Affinity_Red);
        RegisterID(GR.Animator.PlayerClass, "A_Green", Affinity_Green);
        RegisterID(GR.Animator.PlayerClass, "A_Blue", Affinity_Blue);
        RegisterID(GR.Animator.PlayerClass, "A_Light", Affinity_Light);
        RegisterID(GR.Animator.PlayerClass, "A_Dark", Affinity_Dark);
        RegisterID(GR.Animator.PlayerClass, "A_Star", Affinity_Star);
        RegisterID(GR.Animator.PlayerClass, "A_Gen", Affinity_General);
        RegisterID(GR.Animator.PlayerClass, "A_Seal", Affinity_Sealed);

        RegisterID(null,"HP", Health);
        RegisterID(null,"THP", TempHP);
        RegisterID(null,"MHP", MultiHP);
        RegisterID(null,"DD", DelayedDamage);
        RegisterID(null,"SK", SummoningSickness);
        RegisterID(null,"Random Orb", RandomOrb);
        RegisterID(null,"Orb_S", OrbSlot);
        RegisterID(null,"Grief Seed", GriefSeed);
        RegisterID(null,"T-Knife", ThrowingKnife);
        RegisterID(null,"Gold", Gold);
        RegisterID(null,"INS", Inspiration);
    }

    public void InitializeIcons()
    {
        Relic.SetIcon(AbstractCard.orb_relic);
        Potion.SetIcon(AbstractCard.orb_potion);
        Special.SetIcon(AbstractCard.orb_special);

        CommonImages.Badges badges = GR.Common.Images.Badges;
        Exhaust.SetIcon(badges.Exhaust.Texture(), 6);
        Ethereal.SetIcon(badges.Ethereal.Texture(), 6);
        RetainOnce.SetIcon(badges.RetainOnce.Texture(), 6);
        Retain.SetIcon(badges.Retain.Texture(), 6);
        Innate.SetIcon(badges.Innate.Texture(), 6);
        Delayed.SetIcon(badges.Delayed.Texture(), 6);
        Haste.SetIcon(badges.Haste.Texture(), 6);
        Purge.SetIcon(badges.Purge.Texture(), 6);
        Recast.SetIcon(badges.Recast.Texture(), 6);
        Fading.SetIcon(badges.Fading.Texture(), 6);

        CommonImages.CardIcons icons = GR.Common.Images.Icons;
        Ranged.SetIcon(icons.Ranged.Texture(), 6);
        Elemental.SetIcon(icons.Elemental.Texture(), 6);
        Card.SetIcon(icons.Card.Texture(), 6);
        Piercing.SetIcon(icons.Piercing.Texture(), 6);
        TempHP.SetIcon(icons.TempHP.Texture(), 6);
        MultiHP.SetIcon(icons.MultiHP.Texture(), 6);
        Health.SetIcon(icons.HP.Texture(), 6);
        Block.SetIcon(icons.Block.Texture(), 10).SetIconSizeMulti(1f, 0.9f);

        CommonImages.AffinityIcons affinities = GR.Common.Images.Affinities;
        Affinity_Red.SetIcon(affinities.Red.Texture(), 8);
        Affinity_Green.SetIcon(affinities.Green.Texture(), 8);
        Affinity_Blue.SetIcon(affinities.Blue.Texture(), 8);
        Affinity_Light.SetIcon(affinities.Light.Texture(), 8);
        Affinity_Dark.SetIcon(affinities.Dark.Texture(), 8);
        Affinity_Star.SetIcon(affinities.Star.Texture(), 8);
        Affinity_Sealed.SetIcon(affinities.Seal.Texture(), 8);
        Affinity_General.SetIcon(affinities.General.Texture(), 8);
        RedStance.icon = ForceStance.icon;
        GreenStance.icon = AgilityStance.icon;
        BlueStance.icon = IntellectStance.icon;
        DarkStance.icon = CorruptionStance.icon;
        Affinity_Power.icon = Affinity_Token.icon = Affinity_General.icon;

        CommonImages.Tooltips tooltips = GR.Common.Images.Tooltips;
        ThrowingKnife.SetIcon(tooltips.ThrowingKnife.Texture(), 6);
        GriefSeed.SetIcon(tooltips.GriefSeed.Texture(), 6);
        Lightning.SetIcon(tooltips.Lightning.Texture(), 6);
        Fire.SetIcon(tooltips.Fire.Texture(), 6);
        Plasma.SetIcon(tooltips.Plasma.Texture(), 6);
        Dark.SetIcon(tooltips.Dark.Texture(), 6);
        Frost.SetIcon(tooltips.Frost.Texture(), 6);
        Aether.SetIcon(tooltips.Aether.Texture(), 6);
        Earth.SetIcon(tooltips.Earth.Texture(), 6);
        Chaos.SetIcon(tooltips.Chaos.Texture(), 6);
        RandomOrb.SetIcon(tooltips.RandomOrb.Texture(), 6);
        OrbSlot.SetIcon(tooltips.OrbSlot.Texture(), 6);
        Gold.SetIcon(tooltips.Gold.Texture(), 6);

        LoadFromPower(Burning, new BurningPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(0.95f, 0.95f);
        LoadFromPower(Freezing, new FreezingPower(FakeCharacter.Instance, null, 0));//.SetIconSizeMulti(0.95f, 0.95f);
        LoadFromPower(Poison, new PoisonPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(1.05f, 1f);
        LoadFromPower(Strength, new StrengthPower(FakeCharacter.Instance, 0));
        LoadFromPower(Dexterity, new DexterityPower(FakeCharacter.Instance, 0));
        LoadFromPower(Focus, new FocusPower(FakeCharacter.Instance, 0));
        LoadFromPower(Flight, new PlayerFlightPower(FakeCharacter.Instance, 0));
        LoadFromPower(Metallicize, new MetallicizePower(FakeCharacter.Instance, 0));
        LoadFromPower(PlatedArmor, new PlatedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(EnchantedArmor, new EnchantedArmorPlayerPower(FakeCharacter.Instance, 0));
        LoadFromPower(EnchantedArmor_Classic, new EnchantedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Vitality, new VitalityPower(FakeCharacter.Instance, 0));
        LoadFromPower(Invocation, new InvocationPower(FakeCharacter.Instance, 0));
        LoadFromPower(Envenom, new EnvenomPower(FakeCharacter.Instance, 0));
        LoadFromPower(NoxiousFumes, new NoxiousFumesPower(FakeCharacter.Instance, 0));
        LoadFromPower(Vigor, new VigorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Ritual, new RitualPower(FakeCharacter.Instance, 0, true));
        LoadFromPower(Thorns, new ThornsPower(FakeCharacter.Instance, 0));
        LoadFromPower(FlameBarrier, new FlameBarrierPower(FakeCharacter.Instance, 0));
        LoadFromPower(Blur, new BlurPower(FakeCharacter.Instance, 0));
        LoadFromPower(Artifact, new ArtifactPower(FakeCharacter.Instance, 0));
        LoadFromPower(Malleable, new MalleablePower(FakeCharacter.Instance, 0));
        LoadFromPower(Shackles, new GainStrengthPower(FakeCharacter.Instance, 0));
        LoadFromPower(Constricted, new ConstrictedPower(null, FakeCharacter.Instance, 0));
        LoadFromPower(SupportDamage, new SupportDamagePower(FakeCharacter.Instance, 0));
        LoadFromPower(LockOn, new LockOnPower(FakeCharacter.Instance, 0));
        LoadFromPower(Inspiration, new InspirationPower(FakeCharacter.Instance, 0));
        LoadFromPower(DelayedDamage, new DelayedDamagePower(FakeCharacter.Instance, 0));
        LoadFromPower(CounterAttack, new CounterAttackPower(FakeCharacter.Instance, 0));
        LoadFromPower(Intangible, new IntangiblePlayerPower(FakeCharacter.Instance, 0));
        LoadFromPower(Amplification, new AmplificationPower(FakeCharacter.Instance, 0));
        LoadFromPower(Withering, new WitheringPower(FakeCharacter.Instance, 0));
        LoadFromPower(SummoningSickness, new SummoningSicknessPower(FakeCharacter.Instance, 0));
        LoadFromPower(Mark, new MarkedPower(FakeCharacter.Instance));

        //These use AbstractDungeon.player
        LoadFromPower(Weak, new WeakPower(null, 0, false)).SetIconSizeMulti(1f, 0.9f);
        LoadFromPower(Vulnerable, new VulnerablePower(null, 0, false));
        LoadFromPower(Frail, new FrailPower(null, 0, false));
    }

    public static void RegisterEnergyTooltip(String symbol, TextureAtlas.AtlasRegion region)
    {
        if (region == null)
        {
            Texture texture = GR.GetTexture(GR.Animator.Images.ORB_C_PNG);
            region = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
            //region = new TextureAtlas.AtlasRegion(texture, 2, 2, texture.getWidth()-4, texture.getHeight()-4);
        }

        EYBCardTooltip tooltip = new EYBCardTooltip(TipHelper.TEXT[0], GameDictionary.TEXT[0]);
        tooltip.icon = region;
        CardTooltips.RegisterName(null, symbol, tooltip);
    }

    public static void RegisterPowerTooltip(AbstractPlayer.PlayerClass playerClass, String symbol, String id, AbstractPower power)
    {
        int size = power.img.getWidth(); // width should always be equal to height

        EYBCardTooltip tooltip = CardTooltips.FindByID(playerClass, id);
        if (tooltip == null)
        {
            JUtils.LogError(CommonResources.class, "Could not find tooltip: Symbol: {0}, ID: {1}, Power: {2} ",
                    symbol, id, power.name);
            return;
        }

        tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 3, 5, size-6, size-6);
        //tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 2, 4, size-4, size-4);

        EYBCardTooltip stance = CardTooltips.FindByID(playerClass, id + " Stance");
        if (stance != null)
        {
            stance.icon = tooltip.icon;
        }

        CardTooltips.RegisterName(playerClass, symbol, tooltip);
    }

    private EYBCardTooltip LoadFromPower(EYBCardTooltip tooltip, AbstractPower power)
    {
        EYBPower p = JUtils.SafeCast(power, EYBPower.class);
        if (p == null)
        {
            tooltip.SetIcon(power.region48).SetIconSizeMulti(0.9f, 0.9f);
        }
        else if (p.powerIcon != null)
        {
            tooltip.SetIcon(p.powerIcon);
        }
        else
        {
            tooltip.SetIcon(p.img, 6);
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
