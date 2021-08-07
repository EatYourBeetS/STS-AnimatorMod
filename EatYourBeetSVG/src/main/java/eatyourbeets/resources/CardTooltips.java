package eatyourbeets.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.characters.FakeCharacter;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.powers.animator.*;
import eatyourbeets.powers.common.*;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public class CardTooltips
{
    protected static final HashMap<String, EYBCardTooltip> tooltipIDs = new HashMap<>();
    protected static final HashMap<String, EYBCardTooltip> tooltips = new HashMap<>();

    public EYBCardTooltip Energy = FindByName("[E]").ShowText(false);
    public EYBCardTooltip Starter = FindByID("Starter");
    public EYBCardTooltip Limited = FindByID("Limited");
    public EYBCardTooltip SemiLimited = FindByID("Semi-Limited");
    public EYBCardTooltip Unique = FindByID("~Unique");
    public EYBCardTooltip Elemental = FindByID("~Elemental");
    public EYBCardTooltip Piercing = FindByID("~Piercing");
    public EYBCardTooltip Ranged = FindByID("~Ranged");
    public EYBCardTooltip Damage = FindByID("~Damage");
    public EYBCardTooltip Purge = FindByID("Purge");
    public EYBCardTooltip Intellect = FindByID("Intellect");
    public EYBCardTooltip Force = FindByID("Force");
    public EYBCardTooltip Agility = FindByID("Agility");
    public EYBCardTooltip Willpower = FindByID("Willpower");
    public EYBCardTooltip Blessing = FindByID("Blessing");
    public EYBCardTooltip Corruption = FindByID("Corruption");
    public EYBCardTooltip AgilityStance = FindByID("Agility Stance");
    public EYBCardTooltip ForceStance = FindByID("Force Stance");
    public EYBCardTooltip IntellectStance = FindByID("Intellect Stance");
    public EYBCardTooltip WillpowerStance = FindByID("Willpower Stance");
    public EYBCardTooltip NeutralStance = FindByID("Neutral Stance");
    public EYBCardTooltip SupportDamage = FindByID("Support Damage");
    public EYBCardTooltip Affinity_Star = FindByID("Multicolor");
    public EYBCardTooltip OrbCore = FindByID("~Orb Core");
    public EYBCardTooltip Innate = FindByID("~Innate");
    public EYBCardTooltip Ethereal = FindByID("~Ethereal");
    public EYBCardTooltip Haste = FindByID("~Haste");
    public EYBCardTooltip HasteInfinite = FindByID("~HasteInfinite");
    public EYBCardTooltip Retain = FindByID("~Retain");
    public EYBCardTooltip RetainOnce = FindByID("~RetainOnce").ShowText(false);
    public EYBCardTooltip Exhaust = FindByID("Exhaust").ShowText(false);
    public EYBCardTooltip Channel = FindByID("Channel").ShowText(false);
    public EYBCardTooltip Block = FindByID("Block").ShowText(false);
    public EYBCardTooltip Upgrade = FindByID("Upgrade").ShowText(false);
    public EYBCardTooltip Stance = FindByID("Stance").ShowText(false);
    public EYBCardTooltip Metallicize = FindByID("Metallicize");
    public EYBCardTooltip PlatedArmor = FindByID("Plated Armor");
    public EYBCardTooltip EnchantedArmor = FindByID("Enchanted Armor");
    public EYBCardTooltip Vitality = FindByID("Vitality");
    public EYBCardTooltip Ritual = FindByID("Ritual");
    public EYBCardTooltip TempHP = FindByID("Temporary HP");
    public EYBCardTooltip Weak = FindByID("Weak");
    public EYBCardTooltip Vulnerable = FindByID("Vulnerable");
    public EYBCardTooltip Frail = FindByID("Frail");
    public EYBCardTooltip Poison = FindByID("Poison");
    public EYBCardTooltip Burning = FindByID("Burning");
    public EYBCardTooltip Freezing = FindByID("Freezing");
    public EYBCardTooltip Blinded = FindByID("Blinded");
    public EYBCardTooltip Thorns = FindByID("Thorns");
    public EYBCardTooltip Constricted = FindByID("Constricted");
    public EYBCardTooltip Malleable = FindByID("Malleable");
    public EYBCardTooltip FlameBarrier = FindByID("Flame Barrier");
    public EYBCardTooltip Blur = FindByID("Blur");
    public EYBCardTooltip Artifact = FindByID("Artifact");
    public EYBCardTooltip Shackles = FindByID("Shackles");
    public EYBCardTooltip LockOn = FindByID("~Lock-On");
    public EYBCardTooltip Lightning = FindByID("Lightning");
    public EYBCardTooltip Plasma = FindByID("Plasma");
    public EYBCardTooltip Frost = FindByID("Frost");
    public EYBCardTooltip Fire = FindByID("Fire");
    public EYBCardTooltip Chaos = FindByID("Chaos");
    public EYBCardTooltip Earth = FindByID("Earth");
    public EYBCardTooltip Aether = FindByID("Aether");
    public EYBCardTooltip Water = FindByID("Water");
    public EYBCardTooltip Dark = FindByID("Dark");
    public EYBCardTooltip Aura = FindByID("Aura");
    public EYBCardTooltip Autoplay = FindByID("Autoplay");
    public EYBCardTooltip Regeneration = FindByID("Regeneration");
    public EYBCardTooltip SelfDamage = FindByID("Self Damage");
    public EYBCardTooltip CounterAttack = FindByID("Counterattack");
    public EYBCardTooltip Balance = FindByID("Balance");
    public EYBCardTooltip ElementalMastery = FindByID("Elemental Mastery");
    public EYBCardTooltip ElementalExposure = FindByID("Elemental Exposure");
    public EYBCardTooltip Void = FindByID("Void").ShowText(false);
    public EYBCardTooltip Afterlife = FindByID("Afterlife");
    public EYBCardTooltip Rejuvenation = FindByID("Rejuvenation");

    // No Description
    public EYBCardTooltip Affinity_Red = new EYBCardTooltip("Red Affinity", null);
    public EYBCardTooltip Affinity_Green = new EYBCardTooltip("Green Affinity", null);
    public EYBCardTooltip Affinity_Blue = new EYBCardTooltip("Blue Affinity", null);
    public EYBCardTooltip Affinity_Orange = new EYBCardTooltip("Orange Affinity", null);
    public EYBCardTooltip Affinity_Light = new EYBCardTooltip("Light Affinity", null);
    public EYBCardTooltip Affinity_Dark = new EYBCardTooltip("Dark Affinity", null);
    public EYBCardTooltip Affinity_General = FindByID("Affinity");
    public EYBCardTooltip Affinity_Power = FindByID("Affinity Power");
    public EYBCardTooltip Affinity_Token = FindByID("Affinity Token");
    public EYBCardTooltip RandomOrb = new EYBCardTooltip("Random Orb", null);
    public EYBCardTooltip ThrowingKnife = new EYBCardTooltip(eatyourbeets.cards.animator.special.ThrowingKnife.DATA.Strings.NAME, null);
    public EYBCardTooltip GriefSeed = new EYBCardTooltip(Curse_GriefSeed.DATA.Strings.NAME, null);
    public EYBCardTooltip Gold = new EYBCardTooltip(RewardItem.TEXT[1].trim(), null);

    public static void RegisterID(String id, EYBCardTooltip tooltip)
    {
        tooltipIDs.put(id, tooltip);
        tooltip.id = id;
    }

    public static void RegisterName(String name, EYBCardTooltip tooltip)
    {
        tooltips.put(name, tooltip);
    }

    public static EYBCardTooltip FindByName(String name)
    {
        return tooltips.get(name);
    }

    public static EYBCardTooltip FindByID(String id)
    {
        return tooltipIDs.get(id);
    }

    public static String FindName(EYBCardTooltip tooltip)
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
        RegisterID("A-Red", Affinity_Red);
        RegisterID("A-Green", Affinity_Green);
        RegisterID("A-Blue", Affinity_Blue);
        RegisterID("A-Orange", Affinity_Orange);
        RegisterID("A-Light", Affinity_Light);
        RegisterID("A-Dark", Affinity_Dark);
        RegisterID("A-Star", Affinity_Star);
        RegisterID("A-Gen", Affinity_General);

        RegisterID("Temp HP", TempHP);
        RegisterID("Random Orb", RandomOrb);
        RegisterID("Grief Seed", GriefSeed);
        RegisterID("T-Knife", ThrowingKnife);
        RegisterID("Gold", Gold);
    }

    public void InitializeIcons()
    {
        CommonImages.Badges badges = GR.Common.Images.Badges;
        Exhaust.SetIcon(badges.Exhaust.Texture(), 6);
        Ethereal.SetIcon(badges.Ethereal.Texture(), 6);
        RetainOnce.SetIcon(badges.RetainOnce.Texture(), 6);
        Retain.SetIcon(badges.Retain.Texture(), 6);
        Innate.SetIcon(badges.Innate.Texture(), 6);
        Haste.SetIcon(badges.Haste.Texture(), 6);
        HasteInfinite.SetIcon(badges.HasteInfinite.Texture(), 6);
        Purge.SetIcon(badges.Purge.Texture(), 6);
        Autoplay.SetIcon(badges.Autoplay.Texture(), 6);
        Afterlife.SetIcon(badges.Afterlife.Texture(), 6);

        CommonImages.CardIcons icons = GR.Common.Images.Icons;
        Ranged.SetIcon(icons.Ranged.Texture(), 6);
        Elemental.SetIcon(icons.Elemental.Texture(), 6);
        Piercing.SetIcon(icons.Piercing.Texture(), 6);
        TempHP.SetIcon(icons.TempHP.Texture(), 6);
        Block.SetIcon(icons.Block.Texture(), 10).SetIconSizeMulti(1f, 0.9f);

        CommonImages.AffinityIcons affinities = GR.Common.Images.Affinities;
        Affinity_Red.SetIcon(affinities.Red.Texture(), 8);
        Affinity_Green.SetIcon(affinities.Green.Texture(), 8);
        Affinity_Blue.SetIcon(affinities.Blue.Texture(), 8);
        Affinity_Orange.SetIcon(affinities.Orange.Texture(), 8);
        Affinity_Light.SetIcon(affinities.Light.Texture(), 8);
        Affinity_Dark.SetIcon(affinities.Dark.Texture(), 8);
        Affinity_Star.SetIcon(affinities.Star.Texture(), 8);
        Affinity_General.SetIcon(affinities.General.Texture(), 8);
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
        Water.SetIcon(tooltips.Water.Texture(), 6);
        Chaos.SetIcon(tooltips.Chaos.Texture(), 6);
        RandomOrb.SetIcon(tooltips.RandomOrb.Texture(), 6);
        Gold.SetIcon(tooltips.Gold.Texture(), 6);
        RandomOrb.SetIcon(tooltips.RandomOrb.Texture(), 6);
        Regeneration.SetIcon(tooltips.Regeneration.Texture(), 6);

        LoadFromPower(Burning, new BurningPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(0.95f, 0.95f);
        LoadFromPower(Blinded, new BlindedPower(FakeCharacter.Instance, null, 0));//.SetIconSizeMulti(0.95f, 0.95f);
        LoadFromPower(Freezing, new FreezingPower(FakeCharacter.Instance, null,0)).SetIconSizeMulti(0.95f, 0.95f);
        LoadFromPower(Poison, new PoisonPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(1.05f, 1f);
        LoadFromPower(Metallicize, new MetallicizePower(FakeCharacter.Instance, 0));
        LoadFromPower(PlatedArmor, new PlatedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(EnchantedArmor, new EnchantedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Vitality, new VitalityPower(FakeCharacter.Instance, 0));
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
        LoadFromPower(SelfDamage, new SelfDamagePower(FakeCharacter.Instance, 0));
        LoadFromPower(CounterAttack, new CounterAttackPower(FakeCharacter.Instance, 0));
        LoadFromPower(Balance, new BalancePower(FakeCharacter.Instance, 0));
        LoadFromPower(ElementalMastery, new ElementalMasteryPower(FakeCharacter.Instance, 0));
        LoadFromPower(ElementalExposure, new ElementalExposurePower(FakeCharacter.Instance, 0));
        LoadFromPower(Rejuvenation, new RejuvenationPower(FakeCharacter.Instance, 0));

        //These use AbstractDungeon.player
        LoadFromPower(Weak, new WeakPower(null, 0, false)).SetIconSizeMulti(1f, 0.9f);
        LoadFromPower(Vulnerable, new VulnerablePower(null, 0, false));
        LoadFromPower(Frail, new FrailPower(null, 0, false));
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
