package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.resources.GR;

public class TextureCache
{
    private final String path;
    private final boolean mipmap;
    private Texture texture;

    public TextureCache(String path)
    {
        this(path, false);
    }

    public TextureCache(String path, boolean mipmap)
    {
        this.path = path;
        this.mipmap = mipmap;
    }

    public String Path()
    {
        return path;
    }

    public Texture Texture()
    {
        if (texture == null)
        {
            texture = GR.GetTexture(path, mipmap);
        }

        return texture;
    }
}