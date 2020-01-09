package eatyourbeets.resources.common;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;

public class CommonImages
{
    public Textures Textures;

    public Textures CreateTextures()
    {
        return Textures = new Textures();
    }

    public class Textures
    {
        public Texture UnnamedReignEntrance = GR.GetTexture("images/ui/map/act5Entrance.png");
        public Texture UnnamedReignEntranceOutline = GR.GetTexture("images/ui/map/act5EntranceOutline.png");
    }
}
