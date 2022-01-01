package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.ui.TextureCache;
import pinacolada.powers.affinity.*;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public enum PCLAffinity implements Comparable<PCLAffinity>
{
    Red(0, "Red", "Might", "R", GR.PCL.Images.Affinities.Red),
    Green(1, "Green", "Velocity", "G", GR.PCL.Images.Affinities.Green),
    Blue(2, "Blue", "Wisdom", "B", GR.PCL.Images.Affinities.Blue),
    Orange(3, "Orange", "Endurance", "O", GR.PCL.Images.Affinities.Orange),
    Light(4, "Light", "Invocation", "L", GR.PCL.Images.Affinities.Light),
    Dark(5, "Dark", "Desecration", "D", GR.PCL.Images.Affinities.Dark),
    Silver(6, "Silver", "Technic", "S", GR.PCL.Images.Affinities.Silver),
    Star(-1, "Star", "Multicolor", "A", GR.PCL.Images.Affinities.Star),
    General(-2, "Gen", "Multicolor","W", GR.PCL.Images.Affinities.General);// Don't use directly

    public static final int MAX_ID = 4;

    protected static final TextureCache BorderBG = GR.PCL.Images.Affinities.BorderBG;
    protected static final TextureCache BorderFG = GR.PCL.Images.Affinities.BorderFG;
    protected static final TextureCache BorderLV2 = GR.PCL.Images.Affinities.Border_Strong;
    protected static final TextureCache BorderLV1 = GR.PCL.Images.Affinities.Border_Weak;
    protected static final PCLAffinity[] BASIC_TYPES = new PCLAffinity[6];
    protected static final PCLAffinity[] EXTENDED_TYPES = new PCLAffinity[7];
    protected static final PCLAffinity[] ALL_TYPES = new PCLAffinity[8];

    static
    {
        ALL_TYPES[0] = EXTENDED_TYPES[0] = BASIC_TYPES[0] = Red;
        ALL_TYPES[1] = EXTENDED_TYPES[1] = BASIC_TYPES[1] = Green;
        ALL_TYPES[2] = EXTENDED_TYPES[2] = BASIC_TYPES[2] = Blue;
        ALL_TYPES[3] = EXTENDED_TYPES[3] = BASIC_TYPES[3] = Orange;
        ALL_TYPES[4] = EXTENDED_TYPES[4] = BASIC_TYPES[4] = Light;
        ALL_TYPES[5] = EXTENDED_TYPES[5] = BASIC_TYPES[5] = Dark;
        ALL_TYPES[6] = EXTENDED_TYPES[6] = Silver;
        ALL_TYPES[7] = Star;
    }

    public static PCLAffinity[] Basic()
    {
        return BASIC_TYPES;
    }

    public static PCLAffinity[] Extended()
    {
        return EXTENDED_TYPES;
    }

    public static PCLAffinity[] All()
    {
        return ALL_TYPES;
    }

    public final int ID;
    public final TextureCache Icon;
    public final String Name;
    public final String PowerName;
    public final String PowerSymbol;

    PCLAffinity(int id, String name, String powerName, String powerSymbol, TextureCache icon)
    {
        this.ID = id;
        this.Icon = icon;
        this.Name = name;
        this.PowerName = powerName;
        this.PowerSymbol = powerSymbol;
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

    public AbstractPCLAffinityPower GetPower() {
        switch (this)
        {
            case Red: return new MightPower();
            case Green: return new VelocityPower();
            case Blue: return new WisdomPower();
            case Orange: return new EndurancePower();
            case Light: return new InvocationPower();
            case Dark: return new DesecrationPower();
            case Silver: return new TechnicPower();
            default: return null;
        }
    }

    public TextureRegion GetPowerIcon()
    {
        return GetPowerTooltip().icon;
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

            case Orange: return new Color(0.7f, 0.6f, 0.5f, 1f);

            case Light: return new Color(0.8f, 0.8f, 0.3f, 1f);

            case Dark: return new Color(0.55f, 0.1f, 0.85f, 1);//0.7f, 0.55f, 0.7f, 1f);

            case Silver: return new Color(0.5f, 0.5f, 0.5f, 1f);

            case Star: default: return new Color(0.95f, 0.95f, 0.95f, 1f);
        }
    }

    public String GetAffinitySymbol() {
        return PCLJUtils.Format("A-{0}", Name);
    }

    public String GetFormattedAffinitySymbol() {
        return PCLJUtils.Format("[{0}]", GetAffinitySymbol());
    }

    public String GetFormattedPowerSymbol() {
        return PCLJUtils.Format("[{0}]", PowerSymbol);
    }

    public String GetScalingTooltipID() {
        return PCLJUtils.Format("{0} Scaling", PowerName);
    }

    public String GetStanceTooltipID() {
        return PCLJUtils.Format("{0} Stance", PowerName);
    }

    public static PCLAffinity FromTooltip(PCLCardTooltip tooltip)
    {   //@Formatter: Off
        if (tooltip.Is(GR.Tooltips.Affinity_Red)    ) { return PCLAffinity.Red;     }
        if (tooltip.Is(GR.Tooltips.Affinity_Green)  ) { return PCLAffinity.Green;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Blue)   ) { return PCLAffinity.Blue;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Orange) ) { return PCLAffinity.Orange;  }
        if (tooltip.Is(GR.Tooltips.Affinity_Light)  ) { return PCLAffinity.Light;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Dark)   ) { return PCLAffinity.Dark;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Silver) ) { return PCLAffinity.Silver;  }
        if (tooltip.Is(GR.Tooltips.Multicolor)   ) { return PCLAffinity.Star;    }
        if (tooltip.Is(GR.Tooltips.Affinity_General)) { return PCLAffinity.General; }
        return null;
    }   //@Formatter: On

    public PCLCardTooltip GetTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Affinity_Red;
            case Green: return GR.Tooltips.Affinity_Green;
            case Blue: return GR.Tooltips.Affinity_Blue;
            case Orange: return GR.Tooltips.Affinity_Orange;
            case Light: return GR.Tooltips.Affinity_Light;
            case Dark: return GR.Tooltips.Affinity_Dark;
            case Silver: return GR.Tooltips.Affinity_Silver;
            case Star: return GR.Tooltips.Multicolor;
            case General: return GR.Tooltips.Affinity_General;
            default: throw new EnumConstantNotPresentException(PCLAffinity.class, this.name());
        }
    }

    public PCLCardTooltip GetPowerTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Might;
            case Green: return GR.Tooltips.Velocity;
            case Blue: return GR.Tooltips.Wisdom;
            case Orange: return GR.Tooltips.Endurance;
            case Light: return GR.Tooltips.Invocation;
            case Dark: return GR.Tooltips.Desecration;
            case Silver: return GR.Tooltips.Technic;
            case Star:
            case General:
                return GR.Tooltips.Multicolor;
            default: throw new EnumConstantNotPresentException(PCLAffinity.class, this.name());
        }
    }
}