package eatyourbeets.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.ui.TextureCache;

import java.util.HashMap;

public class CardTooltips
{
    protected final HashMap<String, EYBCardTooltip> tooltipIDs = new HashMap<>();
    protected final HashMap<String, EYBCardTooltip> tooltips = new HashMap<>();

    public EYBCardTooltip Limited;
    public EYBCardTooltip SemiLimited;
    public EYBCardTooltip Unique;
    public EYBCardTooltip Elemental;
    public EYBCardTooltip Piercing;
    public EYBCardTooltip Ranged;
    public EYBCardTooltip Damage;
    public EYBCardTooltip Purge;
    public EYBCardTooltip Intellect;
    public EYBCardTooltip Force;
    public EYBCardTooltip Agility;
    public EYBCardTooltip Spellcaster;
    public EYBCardTooltip MartialArtist;
    public EYBCardTooltip Shapeshifter;
    public EYBCardTooltip OrbCore;
    public EYBCardTooltip Innate;
    public EYBCardTooltip Ethereal;
    public EYBCardTooltip Retain;
    public EYBCardTooltip Haste;
    public EYBCardTooltip Exhaust;
    public EYBCardTooltip Channel;
    public EYBCardTooltip Upgrade;
    public EYBCardTooltip Energy;
    public EYBCardTooltip Metallicize;
    public EYBCardTooltip TempHP;
    public EYBCardTooltip Weak;
    public EYBCardTooltip Vulnerable;
    public EYBCardTooltip Poison;
    public EYBCardTooltip Burning;
    public EYBCardTooltip Thorns;

    public boolean CanAdd(EYBCardTooltip tooltip)
    {
        return tooltip != Channel && tooltip != Upgrade && tooltip != Exhaust && tooltip != Retain && tooltip != Energy;
    }

    public void RegisterID(String id, EYBCardTooltip tooltip)
    {
        tooltipIDs.put(id, tooltip);
    }

    public void RegisterName(String name, EYBCardTooltip tooltip)
    {
        tooltips.put(name, tooltip);
    }

    public void Initialize()
    {
        OrbCore = FindByID("~Orb Core");
        Shapeshifter = FindByID("Shapeshifter");
        MartialArtist = FindByID("Martial Artist");
        Spellcaster = FindByID("Spellcaster");
        Agility = FindByID("Agility");
        Force = FindByID("Force");
        Intellect = FindByID("Intellect");
        Purge = FindByID("Purge");
        Damage = FindByID("~Damage");
        Ranged = FindByID("~Ranged");
        Piercing = FindByID("~Piercing");
        Elemental = FindByID("~Elemental");
        Unique = FindByID("~Unique");
        SemiLimited = FindByID("Semi-Limited");
        Limited = FindByID("Limited");
        Innate = FindByID("~Innate");
        Ethereal = FindByID("~Ethereal");
        Retain = FindByID("~Retain");
        Haste = FindByID("~Haste");
        Exhaust = FindByID("Exhaust");
        Channel = FindByID("Channel");
        Upgrade = FindByID("Upgrade");
        Metallicize = FindByID("Metallicize");
        TempHP = FindByID("Temporary HP");
        Weak = FindByID("Weak");
        Vulnerable = FindByID("Vulnerable");
        Poison = FindByID("Poison");
        Burning = FindByID("Burning");
        Thorns = FindByID("Thorns");

        Energy = FindByName("[E]");
    }

    public void InitializeIcons()
    {
        CommonImages.Badges badges = GR.Common.Images.Badges;
        Exhaust.icon = LoadFromBadge(badges.Exhaust);
        Ethereal.icon = LoadFromBadge(badges.Ethereal);
        Retain.icon = LoadFromBadge(badges.Retain);
        Innate.icon = LoadFromBadge(badges.Innate);
        Haste.icon = LoadFromBadge(badges.Haste);
        Purge.icon = LoadFromBadge(badges.Purge);

        CommonImages.CardIcons icons = GR.Common.Images.Icons;
        Ranged.icon = LoadFromLargeIcon(icons.Ranged);
        Elemental.icon = LoadFromLargeIcon(icons.Elemental);
        Piercing.icon = LoadFromLargeIcon(icons.Piercing);
    }

    public EYBCardTooltip FindByName(String name)
    {
        return tooltips.get(name);
    }

    public EYBCardTooltip FindByID(String id)
    {
        return tooltipIDs.get(id);
    }

    private TextureAtlas.AtlasRegion LoadFromBadge(TextureCache textureCache)
    {
        Texture texture = textureCache.Texture();
        int w = texture.getWidth();
        int h = texture.getHeight();
        return new TextureAtlas.AtlasRegion(texture, w / 6, h / 6, w - (w / 3), h - (h / 3));
    }

    private TextureAtlas.AtlasRegion LoadFromLargeIcon(TextureCache textureCache)
    {
        Texture texture = textureCache.Texture();
        int w = texture.getWidth();
        int h = texture.getHeight();
        return new TextureAtlas.AtlasRegion(texture, w / 6, h / 6, w - (w / 3), h - (h / 3));
    }
}
