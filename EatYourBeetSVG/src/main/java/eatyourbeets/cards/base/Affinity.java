package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;

public enum Affinity implements Comparable<Affinity>
{
    Fire(0, "Fire", GR.Common.Images.Affinities.Fire),
    Air(1, "Air", GR.Common.Images.Affinities.Air),
    Mind(6, "Mind", GR.Common.Images.Affinities.Mind),
    Earth(3, "Earth", GR.Common.Images.Affinities.Earth),
    Light(4, "Light", GR.Common.Images.Affinities.Light),
    Dark(5, "Dark", GR.Common.Images.Affinities.Dark),
    Water(6, "Water", GR.Common.Images.Affinities.Water),
    Poison(7, "Poison", GR.Common.Images.Affinities.Poison),
    Steel(8, "Steel", GR.Common.Images.Affinities.Steel),
    Thunder(9, "Thunder", GR.Common.Images.Affinities.Thunder),
    Nature(10, "Nature", GR.Common.Images.Affinities.Nature),
    Cyber(11, "Cyber", GR.Common.Images.Affinities.Cyber),
    Star(-1, "Star", GR.Common.Images.Affinities.Star),
    General(-2, "General", GR.Common.Images.Affinities.General);// Don't use directly

    public static final int MAX_ID = 4;

    protected static final TextureCache BorderBG = GR.Common.Images.Affinities.BorderBG;
    protected static final TextureCache BorderFG = GR.Common.Images.Affinities.BorderFG;
    protected static final TextureCache BorderLV2 = GR.Common.Images.Affinities.Border;
    protected static final TextureCache BorderLV1 = GR.Common.Images.Affinities.Border_Weak;
    protected static final Affinity[] BASIC_TYPES = new Affinity[12];
    protected static final Affinity[] ALL_TYPES = new Affinity[13];

    static
    {
        ALL_TYPES[0] = BASIC_TYPES[0] = Fire;
        ALL_TYPES[1] = BASIC_TYPES[1] = Air;
        ALL_TYPES[2] = BASIC_TYPES[2] = Mind;
        ALL_TYPES[3] = BASIC_TYPES[3] = Earth;
        ALL_TYPES[4] = BASIC_TYPES[4] = Light;
        ALL_TYPES[5] = BASIC_TYPES[5] = Dark;
        ALL_TYPES[6] = BASIC_TYPES[6] = Water;
        ALL_TYPES[7] = BASIC_TYPES[7] = Poison;
        ALL_TYPES[8] = BASIC_TYPES[8] = Steel;
        ALL_TYPES[9] = BASIC_TYPES[9] = Thunder;
        ALL_TYPES[10] = BASIC_TYPES[10] = Nature;
        ALL_TYPES[11] = BASIC_TYPES[11] = Cyber;
        ALL_TYPES[12] = Star;
    }

    public static Affinity[] Basic()
    {
        return BASIC_TYPES;
    }

    public static Affinity[] All()
    {
        return ALL_TYPES;
    }

    public final int ID;
    public final TextureCache Icon;
    public final String Symbol;

    Affinity(int id, String symbol, TextureCache icon)
    {
        this.ID = id;
        this.Icon = icon;
        this.Symbol = symbol;
    }

    public Texture GetIcon()
    {
        return Icon.Texture();
    }

    public Texture GetBorder(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderLV2 : BorderLV1).Texture();
    }

    public Texture GetBackground(int level, int upgrade)
    {
        return /*this == Star ? null : */((level + upgrade) > 1 ? BorderBG.Texture() : null);
    }

    public Texture GetForeground(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderFG.Texture() : null);
    }

    public TextureRegion GetPowerIcon()
    {
        switch (this)
        {
            case Fire: return GR.Tooltips.FireLevel.icon;

            case Air: return GR.Tooltips.AirLevel.icon;

            case Mind: return GR.Tooltips.WaterLevel.icon;

            case Earth: return GR.Tooltips.EarthLevel.icon;

            case Light: return GR.Tooltips.LightLevel.icon;

            case Dark: return GR.Tooltips.DarkLevel.icon;

            case Water: return GR.Tooltips.WaterLevel.icon;

            case Poison: return GR.Tooltips.FireLevel.icon;

            case Steel: return GR.Tooltips.AirLevel.icon;

            case Thunder: return GR.Tooltips.EarthLevel.icon;

            case Nature: return GR.Tooltips.LightLevel.icon;

            case Cyber: return GR.Tooltips.DarkLevel.icon;

            case Star: return GR.Tooltips.Affinity_Star.icon;

            default: return null;
        }
    }

    public Color GetAlternateColor(float lerp)
    {
        return Color.WHITE.cpy().lerp(GetAlternateColor(), lerp);
    }

    public Color GetAlternateColor()
    {
        switch (this)
        {
            case Fire: return Color.RED;

            case Air: return Color.GREEN;

            case Mind: return Color.PINK;

            case Earth: return Color.ORANGE;

            case Light: return Color.WHITE;

            case Dark: return Color.BLACK;

            case Water: return Color.BLUE;

            case Poison: return Color.PURPLE;

            case Steel: return Color.GRAY;

            case Thunder: return Color.YELLOW;

            case Nature: return Color.OLIVE;

            case Cyber: return Color.CYAN;

            case Star: default: return new Color(0.25f, 0.25f, 0.25f, 1f);
        }
    }

    public static Affinity FromTooltip(EYBCardTooltip tooltip)
    {   //@Formatter: Off
        if (tooltip.Is(GR.Tooltips.Affinity_Fire)    ) { return Affinity.Fire;     }
        if (tooltip.Is(GR.Tooltips.Affinity_Air)  ) { return Affinity.Air;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Mind)   ) { return Affinity.Mind;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Earth) ) { return Affinity.Earth;  }
        if (tooltip.Is(GR.Tooltips.Affinity_Light)  ) { return Affinity.Light;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Dark)   ) { return Affinity.Dark;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Water)   ) { return Affinity.Water;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Poison)   ) { return Affinity.Poison;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Steel)   ) { return Affinity.Steel;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Thunder)   ) { return Affinity.Thunder;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Nature)   ) { return Affinity.Nature;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Cyber)   ) { return Affinity.Cyber;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Star)   ) { return Affinity.Star;    }
        if (tooltip.Is(GR.Tooltips.Affinity_General)) { return Affinity.General; }
        return null;
    }   //@Formatter: On

    public EYBCardTooltip GetTooltip()
    {
        switch (this)
        {
            case Fire: return GR.Tooltips.Affinity_Fire;
            case Air: return GR.Tooltips.Affinity_Air;
            case Mind: return GR.Tooltips.Affinity_Mind;
            case Earth: return GR.Tooltips.Affinity_Earth;
            case Light: return GR.Tooltips.Affinity_Light;
            case Dark: return GR.Tooltips.Affinity_Dark;
            case Water: return GR.Tooltips.Affinity_Water;
            case Poison: return GR.Tooltips.Affinity_Poison;
            case Steel: return GR.Tooltips.Affinity_Steel;
            case Thunder: return GR.Tooltips.Affinity_Thunder;
            case Nature: return GR.Tooltips.Affinity_Nature;
            case Cyber: return GR.Tooltips.Affinity_Cyber;
            case Star: return GR.Tooltips.Affinity_Star;
            case General: return GR.Tooltips.Affinity_General;
            default: throw new EnumConstantNotPresentException(Affinity.class, this.name());
        }
    }
}