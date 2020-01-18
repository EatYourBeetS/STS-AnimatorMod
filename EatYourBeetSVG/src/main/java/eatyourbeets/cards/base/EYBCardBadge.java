package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;

public class EYBCardBadge
{
    public static final EYBCardBadge Synergy = new EYBCardBadge(0, "images/cardui/eyb/badges/Synergy.png");
    public static final EYBCardBadge Discard = new EYBCardBadge(1, "images/cardui/eyb/badges/Discard.png");
    public static final EYBCardBadge Exhaust = new EYBCardBadge(2, "images/cardui/eyb/badges/Exhaust.png");
    public static final EYBCardBadge Drawn = new EYBCardBadge(3, "images/cardui/eyb/badges/Drawn.png");
    public static final EYBCardBadge Special = new EYBCardBadge(4, "images/cardui/eyb/badges/Special.png");

    public final int id;
    public final Texture texture;
    public final String description;
    public final String name;

    private EYBCardBadge(int id, String texturePath)
    {
        this.name = GR.Common.Strings.CardBadges.GetName(id);
        this.description = GR.Common.Strings.CardBadges.GetDescription(id);
        this.texture = GR.GetTexture(texturePath);
        this.id = id;
    }
}
