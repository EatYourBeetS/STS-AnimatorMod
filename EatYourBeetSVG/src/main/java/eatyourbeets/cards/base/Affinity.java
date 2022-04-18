package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;

public enum Affinity implements Comparable<Affinity>
{
    Red(0, "Red", GR.Common.Images.Affinities.Red),
    Green(1, "Green", GR.Common.Images.Affinities.Green),
    Blue(2, "Blue", GR.Common.Images.Affinities.Blue),
    Light(3, "Light", GR.Common.Images.Affinities.Light),
    Dark(4, "Dark", GR.Common.Images.Affinities.Dark),
    Star(-1, "Star", GR.Common.Images.Affinities.Star),
    General(-2, "General", GR.Common.Images.Affinities.General),
    Sealed(-3, "Sealed", GR.Common.Images.Affinities.Seal);

    public static final int MAX_ID = 4;

    protected static final TextureCache BorderBG = GR.Common.Images.Affinities.BorderBG;
    protected static final TextureCache BorderFG = GR.Common.Images.Affinities.BorderFG;
    protected static final TextureCache BorderLV2 = GR.Common.Images.Affinities.Border;
    protected static final TextureCache BorderLV1 = GR.Common.Images.Affinities.Border_Weak;
    protected static final TextureCache BorderSealed = GR.Common.Images.Affinities.Border_Seal;
    protected static final Affinity[] BASIC_TYPES = new Affinity[5];
    protected static final Affinity[] ALL_TYPES = new Affinity[6];

    static
    {
        ALL_TYPES[0] = BASIC_TYPES[0] = Red;
        ALL_TYPES[1] = BASIC_TYPES[1] = Green;
        ALL_TYPES[2] = BASIC_TYPES[2] = Blue;
        ALL_TYPES[3] = BASIC_TYPES[3] = Light;
        ALL_TYPES[4] = BASIC_TYPES[4] = Dark;
        ALL_TYPES[5] = Star;
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
    public final String Name;

    Affinity(int id, String name, TextureCache icon)
    {
        this.ID = id;
        this.Icon = icon;
        this.Name = name;
    }

    public Texture GetIcon()
    {
        return Icon.Texture();
    }

    public Texture GetBorder(int level)
    {
        return (this == Sealed ? BorderSealed : level > 1 ? BorderLV2 : BorderLV1).Texture();
    }

    public Texture GetBackground(int level, int upgrade)
    {
        return (this != Sealed && ((level + upgrade) > 1)) ? BorderBG.Texture() : null;
    }

    public Texture GetForeground(int level)
    {
        return (this != Sealed && level > 1 ? BorderFG.Texture() : null);
    }

    public TextureRegion GetPowerIcon()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Force.icon;

            case Green: return GR.Tooltips.Agility.icon;

            case Blue: return GR.Tooltips.Intellect.icon;

            case Light: return GR.Tooltips.Blessing.icon;

            case Dark: return GR.Tooltips.Corruption.icon;

            case Star: return GR.Tooltips.Affinity_Star.icon;

            case General: return GR.Tooltips.Affinity_General.icon;

            case Sealed: return GR.Tooltips.Affinity_Sealed.icon;

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
            case Red: return new Color(0.8f, 0.5f, 0.5f, 1f);

            case Green: return new Color(0.45f, 0.7f, 0.55f, 1f);

            case Blue: return new Color(0.45f, 0.55f, 0.7f, 1f);

            case Light: return new Color(0.8f, 0.8f, 0.3f, 1f);

            case Dark: return new Color(0.55f, 0.1f, 0.85f, 1);//0.7f, 0.55f, 0.7f, 1f);

            case Star: default: return new Color(0.25f, 0.25f, 0.25f, 1f);
        }
    }

    public static Affinity FromTooltip(EYBCardTooltip tooltip)
    {   //@Formatter: Off
        if (tooltip.Is(GR.Tooltips.Affinity_Red)    ) { return Affinity.Red;     }
        if (tooltip.Is(GR.Tooltips.Affinity_Green)  ) { return Affinity.Green;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Blue)   ) { return Affinity.Blue;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Light)  ) { return Affinity.Light;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Dark)   ) { return Affinity.Dark;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Star)   ) { return Affinity.Star;    }
        if (tooltip.Is(GR.Tooltips.Affinity_General)) { return Affinity.General; }
        if (tooltip.Is(GR.Tooltips.Affinity_Sealed) ) { return Affinity.Sealed; }
        return null;
    }   //@Formatter: On

    public EYBCardTooltip GetTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Affinity_Red;
            case Green: return GR.Tooltips.Affinity_Green;
            case Blue: return GR.Tooltips.Affinity_Blue;
            case Light: return GR.Tooltips.Affinity_Light;
            case Dark: return GR.Tooltips.Affinity_Dark;
            case Star: return GR.Tooltips.Affinity_Star;
            case General: return GR.Tooltips.Affinity_General;
            case Sealed: return GR.Tooltips.Affinity_Sealed;

            default: throw new EnumConstantNotPresentException(Affinity.class, this.name());
        }
    }

    public EYBCardTooltip GetScalingTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.ForceScaling;
            case Green: return GR.Tooltips.AgilityScaling;
            case Blue: return GR.Tooltips.IntellectScaling;
            case Light: return GR.Tooltips.BlessingScaling;
            case Dark: return GR.Tooltips.CorruptionScaling;
            case Star: return GR.Tooltips.MulticolorScaling;

            default: throw new EnumConstantNotPresentException(Affinity.class, this.name());
        }
    }

    public EYBCardTooltip GetPowerTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Force;
            case Green: return GR.Tooltips.Agility;
            case Blue: return GR.Tooltips.Intellect;
            case Light: return GR.Tooltips.Blessing;
            case Dark: return GR.Tooltips.Corruption;

            default: throw new EnumConstantNotPresentException(Affinity.class, this.name());
        }
    }

    public AbstractAffinityPower GetPower()
    {
        return CombatStats.Affinities.GetPower(this);
    }
}