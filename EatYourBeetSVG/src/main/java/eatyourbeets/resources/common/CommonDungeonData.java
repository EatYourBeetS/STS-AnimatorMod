package eatyourbeets.resources.common;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class CommonDungeonData implements CustomSavable<CommonDungeonData>
{
    transient Random rng;

    protected Map<String, String> HashMap = null;
    protected Integer RNGCounter = 0;
    protected Boolean EnteredUnnamedReign = false;
    protected Boolean IsCheating = false;

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

    public void SetCheating(boolean value)
    {
        IsCheating = value;
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

    public boolean IsCheating()
    {
        return IsCheating;
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
            IsCheating = data.IsCheating;
            rng = data.rng;
        }
        else
        {
            HashMap = new HashMap<>();
            EnteredUnnamedReign = false;
            RNGCounter = 0;
            IsCheating = false;
            rng = null;
        }
    }

    protected void Validate()
    {
        if (HashMap == null)
        {
            HashMap = new HashMap<>();
        }

        if (IsCheating == null)
        {
            IsCheating = false;
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

    public AbstractCard GetRandomRewardCard(ArrayList<AbstractCard> ignore, boolean includeRares, boolean ignoreCurrentRoom)
    {
        AbstractCard replacement = null;
        boolean searchingCard = true;

        while (searchingCard)
        {
            searchingCard = false;

            final AbstractCard temp = GetRandomRewardCard(includeRares, ignoreCurrentRoom);
            if (temp == null)
            {
                break;
            }

            if (ignore != null)
            {
                for (AbstractCard c : ignore)
                {
                    if (temp.cardID.equals(c.cardID))
                    {
                        searchingCard = true;
                    }
                }
            }

            if (temp instanceof OnAddingToCardRewardListener && ((OnAddingToCardRewardListener) temp).ShouldCancel())
            {
                searchingCard = true;
            }

            if (!searchingCard)
            {
                replacement = temp.makeCopy();
            }
        }

        for (AbstractRelic r : player.relics)
        {
            r.onPreviewObtainCard(replacement);
        }

        return replacement;
    }

    private AbstractCard GetRandomRewardCard(boolean includeRares, boolean ignoreCurrentRoom)
    {
        ArrayList<AbstractCard> list;

        int roll = AbstractDungeon.cardRng.random(100);
        if (includeRares && (roll <= 4 || (!ignoreCurrentRoom && GameUtilities.GetCurrentRoom() instanceof MonsterRoomBoss)))
        {
            list = AbstractDungeon.srcRareCardPool.group;
        }
        else if (roll < 40)
        {
            list = AbstractDungeon.srcUncommonCardPool.group;
        }
        else
        {
            list = AbstractDungeon.srcCommonCardPool.group;
        }

        if (list != null && list.size() > 0)
        {
            return list.get(AbstractDungeon.cardRng.random(list.size() - 1));
        }

        return null;
    }
}
