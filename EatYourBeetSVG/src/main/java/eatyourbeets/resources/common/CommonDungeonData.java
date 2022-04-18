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
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.PersistentCardModifiers;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class CommonDungeonData implements CustomSavable<CommonDungeonData>
{
    transient Random rng;

    protected Map<String, String> HashMap = new HashMap<>();
    protected Integer RNGCounter = 0;
    protected Boolean EnteredUnnamedReign = false;
    protected Boolean IsCheating = false;

    public static CommonDungeonData Register(String id)
    {
        CommonDungeonData data = new CommonDungeonData();
        BaseMod.addSaveField(id, data);
        return data;
    }

    public void DeleteData(String key)
    {
        HashMap.remove(key);
    }

    public void SetData(String key, Object value)
    {
        HashMap.put(key, value == null ? "" : value.toString());
    }

    public String GetString(String key, String defaultValue)
    {
        return HashMap.getOrDefault(key, defaultValue);
    }

    public Integer GetInteger(String key, Integer defaultValue)
    {
        return JUtils.ParseInt(HashMap.getOrDefault(key, null), defaultValue);
    }

    public Float GetFloat(String key, Float defaultValue)
    {
        return JUtils.ParseFloat(HashMap.getOrDefault(key, null), defaultValue);
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

    public void SetCheating()
    {
        if (BooleanUtils.isFalse(IsCheating))
        {
            JUtils.LogInfo(this, "Cheating detected.");
            IsCheating = true;
        }
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
        return BooleanUtils.isTrue(EnteredUnnamedReign);
    }

    public boolean IsCheating()
    {
        return BooleanUtils.isTrue(IsCheating);
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
        PersistentCardModifiers.OnSave(this);
        return this;
    }

    @Override
    public void onLoad(CommonDungeonData commonData)
    {
        Import(commonData);
        Validate();
        PersistentCardModifiers.OnLoad(this);
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

    public AbstractCard TryReplaceFirstCardReward(ArrayList<RewardItem> rewards, float chances, FuncT1<Boolean, AbstractCard> filter, EYBCardData replacement)
    {
        return TryReplaceFirstCardReward(rewards, chances, filter, false, replacement);
    }

    public AbstractCard TryReplaceFirstCardReward(ArrayList<RewardItem> rewards, float chances, boolean sameRarity, EYBCardData replacement)
    {
        return TryReplaceFirstCardReward(rewards, chances, null, sameRarity, replacement);
    }

    public AbstractCard TryReplaceFirstCardReward(ArrayList<RewardItem> rewards, float chances, FuncT1<Boolean, AbstractCard> filter, boolean sameRarity, EYBCardData replacement)
    {
        if (!AbstractDungeon.cardRandomRng.randomBoolean(chances))// || GameUtilities.GetMasterDeckCopies(common.ID).size() >= common.MaxCopies)
        {
            return null;
        }

        for (RewardItem r : rewards)
        {
            if (r.type == RewardItem.RewardType.CARD)
            {
                int index = -1;
                for (int i = 0; i < r.cards.size(); i++)
                {
                    final AbstractCard c = r.cards.get(i);
                    if (replacement.ID.equals(c.cardID))
                    {
                        return null;
                    }

                    if ((!sameRarity || replacement.CardRarity.equals(c.rarity)) && (filter == null || filter.Invoke(c)))
                    {
                        index = i;
                    }
                }

                if (index >= 0)
                {
                    final AbstractCard c = replacement.MakeCopy(r.cards.get(index).upgraded);
                    for (AbstractRelic relic : player.relics)
                    {
                        relic.onPreviewObtainCard(c);
                    }

                    r.cards.set(index, c);

                    return c;
                }
            }
        }

        return null;
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

            if (temp instanceof OnAddingToCardRewardListener && ((OnAddingToCardRewardListener) temp).ShouldCancel()
            || (temp instanceof EYBCard && ((EYBCard) temp).cardData.ShouldCancel()))
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
