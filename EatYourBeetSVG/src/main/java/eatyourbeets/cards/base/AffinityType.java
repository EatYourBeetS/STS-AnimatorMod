package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.Testing;

public enum AffinityType implements Comparable<AffinityType>
{
    Red(0, GR.Common.Images.Affinities.Red),
    Green(1, GR.Common.Images.Affinities.Green),
    Blue(2, GR.Common.Images.Affinities.Blue),
    Light(3, GR.Common.Images.Affinities.Light),
    Dark(4, GR.Common.Images.Affinities.Dark),
    Star(-1, GR.Common.Images.Affinities.Star_BG);

    public static final int MAX_ID = 4;

    protected static final TextureCache BorderBG = GR.Common.Images.Affinities.BorderBG;
    protected static final TextureCache BorderFG = GR.Common.Images.Affinities.BorderFG;
    protected static final TextureCache BorderLV2 = GR.Common.Images.Affinities.Border;
    protected static final TextureCache BorderLV1 = GR.Common.Images.Affinities.Border_Weak;
    protected static final AffinityType[] BASIC_TYPES = new AffinityType[5];
    protected static final AffinityType[] ALL_TYPES = new AffinityType[6];

    static
    {
        ALL_TYPES[0] = Star;
        ALL_TYPES[1] = BASIC_TYPES[0] = Red;
        ALL_TYPES[2] = BASIC_TYPES[1] = Green;
        ALL_TYPES[3] = BASIC_TYPES[2] = Blue;
        ALL_TYPES[4] = BASIC_TYPES[3] = Light;
        ALL_TYPES[5] = BASIC_TYPES[4] = Dark;
    }

    public static AffinityType[] BasicTypes()
    {
        return BASIC_TYPES;
    }

    public static AffinityType[] AllTypes()
    {
        return ALL_TYPES;
    }

    public final int ID;
    public final TextureCache Icon;

    AffinityType(int id, TextureCache icon)
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
        return /*this == Star ? null : */(level > 1 ? BorderLV2 : BorderLV1).Texture();
    }

    public Texture GetBackground(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderBG.Texture() : null);
    }

    public Texture GetForeground(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderFG.Texture() : null);
    }

    public AbstractGameAction QueueSynergyEffect(GameActions actions)
    {
        return actions.StackAffinityPower(this, 1, false);
    }

    public TextureRegion GetSynergyEffectIcon()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Force.icon;

            case Green: return GR.Tooltips.Agility.icon;

            case Blue: return GR.Tooltips.Intellect.icon;

            case Light: return GR.Tooltips.Blessing.icon;

            case Dark: return GR.Tooltips.Corruption.icon;

            case Star: default: return null;
        }
    }

    public Color GetAlternateColor()
    {
        Float[] values = Testing.GetValues();
        if (values != null && values.length >= 3)
        {
            return new Color(values[0], values[1], values[2], 1);
        }

        switch (this)
        {
            case Red: return new Color(0.8f, 0.5f, 0.5f, 1f);

            case Green: return new Color(0.45f, 0.7f, 0.55f, 1f);

            case Blue: return new Color(0.45f, 0.55f, 0.7f, 1f);

            case Light: return new Color(0.8f, 0.8f, 0.3f, 1f);

            case Dark: return new Color(0.7f, 0.55f, 0.7f, 1f);

            case Star: default: return new Color(0.25f, 0.25f, 0.25f, 1f);
        }
    }
}