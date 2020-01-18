package eatyourbeets.resources.common;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.dungeons.TheUnnamedReign;

import java.util.HashMap;
import java.util.Map;

public class CommonDungeonData implements CustomSavable<CommonDungeonData>
{
    transient Random rng;

    protected Map<String, String> HashMap = null;
    protected Integer RNGCounter = 0;
    protected Boolean EnteredUnnamedReign = false;

    public static CommonDungeonData Register(String id)
    {
        CommonDungeonData data = new CommonDungeonData();
        BaseMod.addSaveField(id, data);
        return data;
    }

    public Random GetRNG()
    {
        if (rng == null)
        {
            rng = new Random(Settings.seed);
            rng.setCounter(RNGCounter);
        }

        return rng;
    }

    public void EnterUnnamedReign()
    {
        if (!EnteredUnnamedReign)
        {
            AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
            CardCrawlGame.nextDungeon = TheUnnamedReign.ID;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            GenericEventDialog.hide();
            CardCrawlGame.music.fadeAll();
            AbstractDungeon.fadeOut();
            AbstractDungeon.isDungeonBeaten = true;
        }

        EnteredUnnamedReign = true;
    }

    public boolean IsUnnamedReign()
    {
        return EnteredUnnamedReign;
    }

    public void Reset()
    {
        Import(null);
        Validate();
    }

    @Override
    public CommonDungeonData onSave()
    {
        Validate();
        return this;
    }

    @Override
    public void onLoad(CommonDungeonData commonData)
    {
        Import(commonData);
        Validate();
    }

    protected void Import(CommonDungeonData data)
    {
        if (data != null)
        {
            HashMap = new HashMap<>(data.HashMap);
            EnteredUnnamedReign = data.EnteredUnnamedReign;
            RNGCounter = data.RNGCounter;
            rng = data.rng;
        }
        else
        {
            HashMap = new HashMap<>();
            EnteredUnnamedReign = false;
            RNGCounter = 0;
            rng = null;
        }
    }

    protected void Validate()
    {
        if (HashMap == null)
        {
            HashMap = new HashMap<>();
        }

        if (rng != null)
        {
            RNGCounter = rng.counter;
        }
        else if (RNGCounter == null)
        {
            RNGCounter = 0;
        }

        if (EnteredUnnamedReign == null)
        {
            EnteredUnnamedReign = false;
        }
    }
}
