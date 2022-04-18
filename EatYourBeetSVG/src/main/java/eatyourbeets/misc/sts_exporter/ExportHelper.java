package eatyourbeets.misc.sts_exporter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class ExportHelper
{
    public ExportHelper()
    {
        this.dir = "sts_exporter";
        this.include_basegame = false;
        this.render_images = true;
        initModList();
    }

    // ----------------------------------------------------------------------------
    // Exporting
    // ----------------------------------------------------------------------------

    // Target directory and config
    protected String dir;
    protected boolean include_basegame;
    protected boolean render_images;

    // Export all collected items
    void exportAll()
    {
        if (render_images)
        {
            exportAllImages();
        }
    }

    void exportAllImages()
    {
        for (ModExportData mod : mods)
        {
            if (modIncludedInExport(mod))
            {
                mod.exportImages();
            }
        }
    }

    String exportDir(ModExportData mod)
    {
        return dir + "/" + mod.id;
    }

    String exportDir(ColorExportData color)
    {
        return dir + "/colors/" + color.id;
    }

    ExportPath exportPath(ModExportData mod, String dir, String id, String suffix)
    {
        if (id.startsWith(mod.id + ":"))
        {
            id = id.substring(mod.id.length() + 1); // strip mod ids
        }
        String file = makeFilename(id) + suffix;
        return new ExportPath(this.dir, mod.id, dir, file);
    }

    private static String makeFilename(String id)
    {
        id = id.replaceAll(":", "-");
        id = id.replace("+", "Plus");
        id = id.replace("*", "Star");
        return id.replaceAll("[\\s\\\\/:*?\"\'<>|+%]", "");
    }

    // ----------------------------------------------------------------------------
    // Per mod items
    // ----------------------------------------------------------------------------

    // Per mod items
    private ArrayList<ModExportData> mods = new ArrayList<>();
    // Combined items
    public ArrayList<CardExportData> cards = new ArrayList<>();
    public ArrayList<RelicExportData> relics = new ArrayList<>();
    public ArrayList<CreatureExportData> creatures = new ArrayList<>();
    public ArrayList<PotionExportData> potions = new ArrayList<>();
    public ArrayList<ColorExportData> colors = new ArrayList<>();
    public ArrayList<KeywordExportData> keywords = new ArrayList<>();

    private void initModList()
    {
        mods.add(new ModExportData(this));
        for (ModInfo modInfo : Loader.MODINFOS)
        {
            mods.add(new ModExportData(this, modInfo));
        }
    }

    public ModExportData findMod(Class<?> cls)
    {
        // Inspired by BaseMod.patches.whatmod.WhatMod
        if (cls == null)
        {
            return mods.get(0);
        }
        URL locationURL = cls.getProtectionDomain().getCodeSource().getLocation();
        if (locationURL == null)
        {
            return findMod(cls.getName());
        }
        else
        {
            return findMod(locationURL);
        }
    }

    public ModExportData findMod(String clsName)
    {
        if (clsName == null)
        {
            return mods.get(0);
        }
        try
        {
            ClassPool pool = Loader.getClassPool();
            CtClass ctCls = pool.get(clsName);
            String url = ctCls.getURL().getFile();
            int i = url.lastIndexOf('!');
            url = url.substring(0, i);
            URL locationURL = new URL(url);
            return findMod(locationURL);
        }
        catch (NotFoundException | MalformedURLException e)
        {
            e.printStackTrace();
            return mods.get(0);
        }
    }

    public ModExportData findMod(URL locationURL)
    {
        if (locationURL == null)
        {
            return mods.get(0);
        }
        for (ModExportData mod : mods)
        {
            if (locationURL.equals(mod.url))
            {
                return mod;
            }
        }
        return mods.get(0);
    }

    public boolean modIncludedInExport(ModExportData mod)
    {
        if (mod.id.equals(ModExportData.BASE_GAME_ID))
        {
            return include_basegame;
        }
        else
        {
            return true;
        }
    }

    public ColorExportData findColor(CardColor color)
    {
        for (ColorExportData c : this.colors)
        {
            if (c.color == color)
            {
                return c;
            }
        }
        return null;
    }

    private static ArrayList<CardExportData> withUpgrades(ArrayList<CardExportData> cards)
    {
        ArrayList<CardExportData> all = new ArrayList<>(cards);
        for (CardExportData card : cards)
        {
            if (card.upgrade != null)
            {
                all.add(card.upgrade);
            }
        }
        Collections.sort(all);
        return all;
    }

    // ----------------------------------------------------------------------------
    // Image exporting
    // ----------------------------------------------------------------------------

    public static void renderSpriteBatchToPNG(float x, float y, float width, float height, float scale, String imageFile, Consumer<SpriteBatch> render)
    {
        renderSpriteBatchToPNG(x, y, width, height, Math.round(scale * width), Math.round(scale * height), imageFile, render);
    }

    public static void renderSpriteBatchToPNG(float x, float y, float width, float height, int iwidth, int iheight, String imageFile, Consumer<SpriteBatch> render)
    {
        renderSpriteBatchToPixmap(x, y, width, height, iwidth, iheight, render, (Pixmap pixmap) -> PixmapIO.writePNG(Gdx.files.local(imageFile), pixmap));
    }

    public static void renderSpriteBatchToPNG(float x, float y, float width, float height, float scale, String imageFile, Consumer<SpriteBatch> render, Consumer<Pixmap> write)
    {
        renderSpriteBatchToPNG(x, y, width, height, Math.round(scale * width), Math.round(scale * height), imageFile, render, write);
    }

    public static void renderSpriteBatchToPNG(float x, float y, float width, float height, int iwidth, int iheight, String imageFile, Consumer<SpriteBatch> render, Consumer<Pixmap> write)
    {
        renderSpriteBatchToPixmap(x, y, width, height, iwidth, iheight, render, write);
    }

    public static void renderSpriteBatchToPixmap(float x, float y, float width, float height, float scale, Consumer<SpriteBatch> render, Consumer<Pixmap> write)
    {
        renderSpriteBatchToPixmap(x, y, width, height, Math.round(scale * width), Math.round(scale * height), render, write);
    }

    public static void renderSpriteBatchToPixmap(float x, float y, float width, float height, int iwidth, int iheight, Consumer<SpriteBatch> render, Consumer<Pixmap> write)
    {
        // create a frame buffer
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, iwidth, iheight, false);
        //make the FBO the current buffer
        fbo.begin();
        try
        {
            //... clear the FBO color with transparent black ...
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f); //transparent black
            Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT); //clear the color buffer
            // set up batch and projection matrix
            SpriteBatch sb = new SpriteBatch();
            Matrix4 matrix = new Matrix4();
            matrix.setToOrtho(x, x + width, y + height, y, 0.f, 1.f); // note: flip the vertical direction, otherwise cards are upside down
            sb.setProjectionMatrix(matrix);
            // render the thing
            sb.begin();
            try
            {
                render.accept(sb);
            }
            finally
            {
                sb.end();
                sb.dispose();
            }
            // write to png file
            Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, iwidth, iheight);
            try
            {
                write.accept(pixmap);
            }
            finally
            {
                pixmap.dispose();
            }
        }
        finally
        {
            // done
            fbo.end();
            fbo.dispose();
        }
    }

    public static Pixmap resizePixmap(Pixmap pixmap, int width, int height)
    {
        Pixmap resized = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setFilter(Pixmap.Filter.BiLinear);
        resized.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, width, height);
        return resized;
    }
}