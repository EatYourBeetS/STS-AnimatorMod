package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;

public class TextureCache
{
    private final String path;
    private Texture texture;

    public TextureCache(String path)
    {
        this.path = path;
    }

    public String Path()
    {
        return path;
    }

    public Texture Texture()
    {
        if (texture == null)
        {
            texture = GR.GetTexture(path);
        }

        return texture;
    }
}
