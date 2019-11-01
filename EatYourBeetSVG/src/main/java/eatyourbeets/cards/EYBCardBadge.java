package eatyourbeets.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import eatyourbeets.resources.AbstractResources;

public class EYBCardBadge
{
    public static final EYBCardBadge Synergy = new EYBCardBadge(0, "images/cardui/common/badges/Synergy.png");
    public static final EYBCardBadge Discard = new EYBCardBadge(1, "images/cardui/common/badges/Discard.png");
    public static final EYBCardBadge Exhaust = new EYBCardBadge(2, "images/cardui/common/badges/Exhaust.png");
    public static final EYBCardBadge Drawn   = new EYBCardBadge(3, "images/cardui/common/badges/Drawn.png"  );
    public static final EYBCardBadge Special = new EYBCardBadge(4, "images/cardui/common/badges/Special.png");

    public final int id;
    public final Texture texture;

    private EYBCardBadge(int id, String texturePath)
    {
        this.texture = AbstractResources.GetTexture(texturePath);
        this.id = id;
    }
}
