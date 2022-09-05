package eatyourbeets.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class EYBRelic extends CustomRelic
{
    private static final FieldInfo<Float> _offsetX = JUtils.GetField("offsetX", AbstractRelic.class);

    public static AbstractPlayer player;
    public static Random rng;

    public EYBCardTooltip mainTooltip;
    public ArrayList<EYBCardTooltip> tips;

    public EYBRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, GR.GetTexture(GR.GetRelicImage(imageID)), tier, sfx);
    }

    public EYBRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, id, tier, sfx);
    }

    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return null;
    }

    public TextureAtlas.AtlasRegion GetPowerIcon()
    {
        final Texture texture = img;
        final int h = texture.getHeight();
        final int w = texture.getWidth();
        final int section = h / 2;
        return new TextureAtlas.AtlasRegion(texture, (w / 2) - (section / 2), (h / 2) - (section / 2), section, section);
    }

    public final void updateDescription()
    {
        updateDescription(false);
    }

    @Override
    public final void updateDescription(AbstractPlayer.PlayerClass c)
    {
        updateDescription(false);
    }

    public final void updateDescription(boolean initializeTips)
    {
        this.description = getUpdatedDescription();

        if (initializeTips)
        {
            initializeTips();
        }
        else
        {
            this.mainTooltip.description = description;
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, counter);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            JUtils.LogError(this, e.getMessage());
            return null;
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel)
    {
        if (this.counter >= 0)
        {
            final String text = GetCounterString();
            if (inTopPanel)
            {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, text,
                        _offsetX.Get(null) + this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
            }
            else
            {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, text,
                        this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
            }
        }
    }

    protected String FormatDescription(int index, Object... args)
    {
        return JUtils.Format(DESCRIPTIONS[index], args);
    }

    protected void DisplayAboveCreature(AbstractCreature creature)
    {
        GameActions.Top.Add(new RelicAboveCreatureAction(creature, this));
    }

    protected String GetCounterString()
    {
        return String.valueOf(counter);
    }

    public boolean IsEnabled()
    {
        return !super.grayscale;
    }

    public boolean SetEnabled(boolean value)
    {
        super.grayscale = !value;
        return value;
    }

    public int SetCounter(int amount)
    {
        setCounter(amount);

        return counter;
    }

    public int AddCounter(int amount)
    {
        setCounter(counter + amount);

        return counter;
    }

    @Override
    public void renderBossTip(SpriteBatch sb)
    {
        EYBCardTooltip.QueueTooltips(tips, Settings.WIDTH * 0.63F, Settings.HEIGHT * 0.63F, false);
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        EYBCardTooltip.QueueTooltips(this);
    }

    @Override
    public void atPreBattle()
    {
        super.atPreBattle();

        ActivateBattleEffect();
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        DeactivateBattleEffect();
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (GameUtilities.InBattle(true))
        {
            ActivateBattleEffect();
        }
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        if (GameUtilities.InBattle(true))
        {
            DeactivateBattleEffect();
        }
    }

    @Override
    protected void initializeTips()
    {
        if (tips == null)
        {
            tips = new ArrayList<>();
        }
        else
        {
            tips.clear();
        }

        mainTooltip = new EYBCardTooltip(name, description);
        tips.add(mainTooltip);

        final AbstractPlayer.PlayerClass playerClass = GetPlayerClass();
        final Scanner desc = new Scanner(this.description);
        String s;
        boolean alreadyExists;
        do
        {
            if (!desc.hasNext())
            {
                desc.close();
                return;
            }

            s = desc.next();
            if (s.charAt(0) == '#')
            {
                s = s.substring(2);
            }

            s = s.replace(',', ' ');
            s = s.replace('.', ' ');

            if (s.length() > 4)
            {
                s = s.replace('[', ' ');
                s = s.replace(']', ' ');
            }

            s = s.trim();
            s = s.toLowerCase();

            EYBCardTooltip tip = CardTooltips.FindByName(playerClass, s);
            if (tip != null && !tips.contains(tip))
            {
                tips.add(tip);
            }
        }
        while (true);
    }

    protected void ActivateBattleEffect()
    {

    }

    protected void DeactivateBattleEffect()
    {

    }
}
