package eatyourbeets.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.characters.FakeCharacter;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public class CardTooltips
{
    protected static final HashMap<String, EYBCardTooltip> tooltipIDs = new HashMap<>();
    protected static final HashMap<String, EYBCardTooltip> tooltips = new HashMap<>();

    public EYBCardTooltip Energy = FindByName("[E]");
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
    public EYBCardTooltip IntellectStance = FindByID("Intellect Stance");
    public EYBCardTooltip ForceStance = FindByID("Force Stance");
    public EYBCardTooltip SupportDamage = FindByID("Support Damage");
    public EYBCardTooltip AgilityStance = FindByID("Agility Stance");
    public EYBCardTooltip Spellcaster = FindByID("Spellcaster");
    public EYBCardTooltip MartialArtist = FindByID("Martial Artist");
    public EYBCardTooltip Shapeshifter = FindByID("Shapeshifter");
    public EYBCardTooltip OrbCore = FindByID("~Orb Core");
    public EYBCardTooltip Innate = FindByID("~Innate");
    public EYBCardTooltip Ethereal = FindByID("~Ethereal");
    public EYBCardTooltip Retain = FindByID("~Retain");
    public EYBCardTooltip Haste = FindByID("~Haste");
    public EYBCardTooltip Exhaust = FindByID("Exhaust");
    public EYBCardTooltip Channel = FindByID("Channel");
    public EYBCardTooltip Block = FindByID("Block");

    public EYBCardTooltip Upgrade = FindByID("Upgrade");
    public EYBCardTooltip Metallicize = FindByID("Metallicize");
    public EYBCardTooltip PlatedArmor = FindByID("Plated Armor");
    public EYBCardTooltip EnchantedArmor = FindByID("Enchanted Armor");
    public EYBCardTooltip TempHP = FindByID("Temporary HP");
    public EYBCardTooltip Weak = FindByID("Weak");
    public EYBCardTooltip Vulnerable = FindByID("Vulnerable");
    public EYBCardTooltip Poison = FindByID("Poison");
    public EYBCardTooltip Burning = FindByID("Burning");
    public EYBCardTooltip Thorns = FindByID("Thorns");
    public EYBCardTooltip Constricted = FindByID("Constricted");
    public EYBCardTooltip FlameBarrier = FindByID("Flame Barrier");
    public EYBCardTooltip Blur = FindByID("Blur");
    public EYBCardTooltip Artifact = FindByID("Artifact");
    public EYBCardTooltip Shackles = FindByID("Shackles");
    public EYBCardTooltip LockOn = FindByID("~Lock-On");
    public EYBCardTooltip Lightning = FindByID("Lightning");
    public EYBCardTooltip Plasma = FindByID("Plasma");
    public EYBCardTooltip Frost = FindByID("Frost");
    public EYBCardTooltip Fire = FindByID("Fire");
    public EYBCardTooltip Earth = FindByID("Earth");
    public EYBCardTooltip Aether = FindByID("Aether");
    public EYBCardTooltip Dark = FindByID("Dark");

    // No Description
    public EYBCardTooltip RandomOrb = new EYBCardTooltip("Random Orb", null);
    public EYBCardTooltip ThrowingKnife = new EYBCardTooltip(eatyourbeets.cards.animator.special.ThrowingKnife.DATA.Strings.NAME, null);
    public EYBCardTooltip Gold = new EYBCardTooltip(TopPanel.LABEL[4], null);

    public static void RegisterID(String id, EYBCardTooltip tooltip)
    {
        tooltipIDs.put(id, tooltip);
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

    public CardTooltips()
    {
        RegisterID("Random Orb", RandomOrb);
        RegisterID("T-Knife", ThrowingKnife);
        RegisterID("Gold", Gold);
    }

    public boolean CanAdd(EYBCardTooltip tooltip)
    {
        return tooltip != null && tooltip.title != null && tooltip != Block
            && tooltip != Channel && tooltip != Upgrade && tooltip != Exhaust
            && tooltip != Retain && tooltip != Energy;
    }

    public void InitializeIcons()
    {
        CommonImages.Badges badges = GR.Common.Images.Badges;
        Exhaust.SetIcon(badges.Exhaust.Texture(), 6);
        Ethereal.SetIcon(badges.Ethereal.Texture(), 6);
        Retain.SetIcon(badges.Retain.Texture(), 6);
        Innate.SetIcon(badges.Innate.Texture(), 6);
        Haste.SetIcon(badges.Haste.Texture(), 6);
        Purge.SetIcon(badges.Purge.Texture(), 6);

        CommonImages.CardIcons icons = GR.Common.Images.Icons;
        Ranged.SetIcon(icons.Ranged.Texture(), 6);
        Elemental.SetIcon(icons.Elemental.Texture(), 6);
        Piercing.SetIcon(icons.Piercing.Texture(), 6);
        TempHP.SetIcon(icons.TempHP.Texture(), 6);

        CommonImages.Tooltips tooltips = GR.Common.Images.Tooltips;
        ThrowingKnife.SetIcon(tooltips.ThrowingKnife.Texture(), 6);
        Lightning.SetIcon(tooltips.Lightning.Texture(), 6);
        Fire.SetIcon(tooltips.Fire.Texture(), 6);
        Plasma.SetIcon(tooltips.Plasma.Texture(), 6);
        Dark.SetIcon(tooltips.Dark.Texture(), 6);
        Frost.SetIcon(tooltips.Frost.Texture(), 6);
        Aether.SetIcon(tooltips.Aether.Texture(), 6);
        Earth.SetIcon(tooltips.Earth.Texture(), 6);
        RandomOrb.SetIcon(tooltips.RandomOrb.Texture(), 6);
        Gold.SetIcon(ImageMaster.UI_GOLD, 6);

        LoadFromPower(Burning, new BurningPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(0.95f, 0.95f);
        LoadFromPower(Poison, new PoisonPower(FakeCharacter.Instance, null, 0)).SetIconSizeMulti(1.05f, 1f);
        LoadFromPower(Metallicize, new MetallicizePower(FakeCharacter.Instance, 0));
        LoadFromPower(PlatedArmor, new PlatedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(EnchantedArmor, new EnchantedArmorPower(FakeCharacter.Instance, 0));
        LoadFromPower(Thorns, new ThornsPower(FakeCharacter.Instance, 0));
        LoadFromPower(FlameBarrier, new FlameBarrierPower(FakeCharacter.Instance, 0));
        LoadFromPower(Blur, new BlurPower(FakeCharacter.Instance, 0));
        LoadFromPower(Artifact, new ArtifactPower(FakeCharacter.Instance, 0));
        LoadFromPower(Shackles, new GainStrengthPower(FakeCharacter.Instance, 0));
        LoadFromPower(Constricted, new ConstrictedPower(null, FakeCharacter.Instance, 0));
        LoadFromPower(SupportDamage, new SupportDamagePower(FakeCharacter.Instance, 0));
        LoadFromPower(LockOn, new LockOnPower(FakeCharacter.Instance, 0));

        //These 2 use AbstractDungeon.player
        LoadFromPower(Weak, new WeakPower(null, 0, false)).SetIconSizeMulti(1f, 0.9f);
        LoadFromPower(Vulnerable, new VulnerablePower(null, 0, false));
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
