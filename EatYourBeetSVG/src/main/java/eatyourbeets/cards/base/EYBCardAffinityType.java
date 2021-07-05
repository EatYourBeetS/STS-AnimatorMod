package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;

public enum EYBCardAffinityType implements Comparable<EYBCardAffinityType>
{
    Red(0, GR.Common.Images.Affinities.Red),
    Green(1, GR.Common.Images.Affinities.Green),
    Blue(2, GR.Common.Images.Affinities.Blue),
    Light(3, GR.Common.Images.Affinities.Light),
    Dark(4, GR.Common.Images.Affinities.Dark),
    Star(-1, GR.Common.Images.Affinities.Star);

    public static final TextureCache BorderLV2 = GR.Common.Images.Affinities.Border;
    public static final TextureCache BorderLV1 = GR.Common.Images.Affinities.Border_Weak;

    public final int ID;
    public final TextureCache Icon;

    EYBCardAffinityType(int id, TextureCache icon)
    {
        this.ID = id;
        this.Icon = icon;
    }

    public Texture GetIcon()
    {
        return Icon.Texture();
    }

    public Texture GetBorder(int level)
    {
        return this == Star ? null : (level > 1 ? BorderLV2 : BorderLV1).Texture();
    }
}