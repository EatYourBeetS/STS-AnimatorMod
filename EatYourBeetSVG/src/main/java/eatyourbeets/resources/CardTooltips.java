package eatyourbeets.resources;

import eatyourbeets.cards.base.EYBCardTooltip;

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
    }

    public EYBCardTooltip FindByName(String name)
    {
        return tooltips.get(name);
    }

    public EYBCardTooltip FindByID(String id)
    {
        return tooltipIDs.get(id);
    }
}
