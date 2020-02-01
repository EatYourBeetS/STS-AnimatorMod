package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.StartActSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Ghosts;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.events.animator.TheMaskedTraveler1;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashSet;

public class AnimatorDungeonData implements CustomSavable<AnimatorDungeonData>, StartGameSubscriber, StartActSubscriber
{
    public transient final ArrayList<AnimatorRuntimeLoadout> Series = new ArrayList<>();
    public transient AnimatorLoadout StartingSeries = new _FakeLoadout();
    public HashSet<String> BannedCards = new HashSet<>();

    protected ArrayList<AnimatorLoadoutProxy> loadouts = new ArrayList<>();
    protected int startingLoadout = -1;

    public static AnimatorDungeonData Register(String id)
    {
        AnimatorDungeonData data = new AnimatorDungeonData();
        BaseMod.addSaveField(id, data);
        BaseMod.subscribe(data);
        return data;
    }

    public void AddSeries(AnimatorRuntimeLoadout series)
    {
        Series.add(series);

        Log("Adding series: " + series.Loadout.Name);
    }

    public void AddAllSeries()
    {
        Series.clear();

        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            AnimatorRuntimeLoadout r = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (r != null)
            {
                Series.add(r);
            }
        }

        FullLog("ADD ALL SERIES");
    }

    public void Reset()
    {
        FullLog( "RESETTING...");

        Series.clear();
        StartingSeries = new _FakeLoadout();
        loadouts.clear();
        startingLoadout = -1;
    }

    @Override
    public AnimatorDungeonData onSave()
    {
        loadouts.clear();

        for (AnimatorRuntimeLoadout loadout : Series)
        {
            AnimatorLoadoutProxy surrogate = new AnimatorLoadoutProxy();
            surrogate.id = loadout.ID;
            surrogate.isBeta = loadout.IsBeta;
            surrogate.promoted = loadout.promoted;
            loadouts.add(surrogate);
        }

        if (StartingSeries.ID > 0)
        {
            startingLoadout = StartingSeries.ID;
        }
        else
        {
            startingLoadout = GR.Animator.Data.SelectedLoadout.ID;
        }

        FullLog("ON SAVE");

        return this;
    }

    @Override
    public void onLoad(AnimatorDungeonData data)
    {
        Series.clear();
        BannedCards.clear();

        if (data != null)
        {
            BannedCards.addAll(data.BannedCards);
            StartingSeries = GR.Animator.Data.GetBaseLoadout(data.startingLoadout);

            for (AnimatorLoadoutProxy proxy : data.loadouts)
            {
                AnimatorRuntimeLoadout loadout = AnimatorRuntimeLoadout.TryCreate(GR.Animator.Data.GetLoadout(proxy.id, proxy.isBeta));
                if (loadout != null)
                {
                    if (proxy.promoted)
                    {
                        loadout.Promote();
                    }

                    loadout.BuildCard();
                    Series.add(loadout);
                }
            }
        }

        if (StartingSeries == null)
        {
            StartingSeries = GR.Animator.Data.SelectedLoadout;
        }

        FullLog("ON LOAD");
    }

    @Override
    public void receiveStartAct()
    {
        FullLog("ON ACT START");
        InitializeCardPool(false);
    }

    @Override
    public void receiveStartGame()
    {
        FullLog("ON GAME START");
        InitializeCardPool(true);
    }

    public void InitializeCardPool(boolean startGame)
    {
        if (AbstractDungeon.player.chosenClass != GR.Animator.PlayerClass)
        {
            AbstractDungeon.srcColorlessCardPool.group.removeIf(c -> c instanceof AnimatorCard);
            AbstractDungeon.colorlessCardPool.group.removeIf(c -> c instanceof AnimatorCard);
            AbstractDungeon.eventList.remove(TheMaskedTraveler1.ID);
            return;
        }

        if (startGame && Settings.isStandardRun())
        {
            GR.Animator.Data.SaveTrophies(true);
        }

        if (GameUtilities.GetActualAscensionLevel() >= 17)
        {
            AbstractDungeon.eventList.remove(Ghosts.ID);
        }

        if (Series.isEmpty())
        {
            return;
        }

        PurgingStone.UpdateBannedCards();

        ArrayList<CardGroup> groups = new ArrayList<>();
        groups.add(AbstractDungeon.commonCardPool);
        groups.add(AbstractDungeon.uncommonCardPool);
        groups.add(AbstractDungeon.srcCommonCardPool);
        groups.add(AbstractDungeon.srcUncommonCardPool);
        groups.add(AbstractDungeon.rareCardPool);
        groups.add(AbstractDungeon.srcRareCardPool);

        for (CardGroup group : groups)
        {
            group.group.removeIf(card ->
            {
                if (card.color != GR.Animator.CardColor)
                {
                    return false;
                }

                if (!BannedCards.contains(card.cardID))
                {
                    for (AnimatorRuntimeLoadout loadout : Series)
                    {
                        if (loadout.Cards.containsKey(card.cardID))
                        {
                            return false;
                        }
                    }
                }

                return true;
            });
        }
    }

    private void Log(String message)
    {
        JavaUtilities.Log(this, message);
    }

    private void FullLog(String message)
    {
        JavaUtilities.Log(this, "================================================================================================");
        JavaUtilities.Log(this, message);
        JavaUtilities.Log(this, "[Transient  Data] Starting Series: " + StartingSeries.Name + ", Series Count: " + Series.size());
        JavaUtilities.Log(this, "[Persistent Data] Starting Series: " + startingLoadout + ", Series Count: " + loadouts.size());
    }

    protected static class AnimatorLoadoutProxy
    {
        public int id;
        public boolean isBeta;
        public boolean promoted;
    }
}
