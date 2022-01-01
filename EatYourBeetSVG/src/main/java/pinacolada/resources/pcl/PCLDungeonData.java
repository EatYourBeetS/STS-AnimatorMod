package pinacolada.resources.pcl;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.StartActSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.interfaces.listeners.OnCardPoolChangedListener;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardBase;
import pinacolada.effects.card.PermanentUpgradeEffect;
import pinacolada.events.base.PCLEvent;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.loadouts._FakeLoadout;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static pinacolada.ui.seriesSelection.PCLLoadoutsContainer.MINIMUM_SERIES;

public class PCLDungeonData implements CustomSavable<PCLDungeonData>, StartGameSubscriber, StartActSubscriber
{
    transient Random rng;

    protected Map<String, String> EventLog = new HashMap<>();
    protected Integer RNGCounter = 0;
    protected Boolean EnteredUnnamedReign = false;
    protected Boolean IsCheating = false;
    protected Integer LongestMatchCombo = 0;
    public transient final ArrayList<PCLRuntimeLoadout> Loadouts = new ArrayList<>();
    public transient PCLLoadout StartingSeries = new _FakeLoadout();
    public HashSet<String> BannedCards = new HashSet<>();

    protected ArrayList<AnimatorLoadoutProxy> loadouts = new ArrayList<>();
    protected int startingLoadout = -1;

    public static PCLDungeonData Register(String id)
    {
        final PCLDungeonData data = new PCLDungeonData();
        BaseMod.addSaveField(id, data);
        BaseMod.subscribe(data);
        return data;
    }

    public String GetMapData(String eventID)
    {
        return (EventLog != null ? EventLog.getOrDefault(eventID, null) : null);
    }

    public void SetMapData(String eventID, Object value)
    {
        EventLog.put(eventID, value.toString());
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
        if (!IsCheating)
        {
            PCLJUtils.LogInfo(this, "Cheating detected.");
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

    public void UpdateLongestMatchCombo(int newCombo) {
        LongestMatchCombo = Math.max(LongestMatchCombo, newCombo);
    }

    public boolean IsUnnamedReign()
    {
        return eatyourbeets.resources.GR.IsLoaded() ? GR.Common.Dungeon.IsUnnamedReign() : EnteredUnnamedReign;
    }

    public boolean IsCheating()
    {
        return IsCheating;
    }

    public int GetLongestMatchCombo() { return LongestMatchCombo; }

    public PCLRuntimeLoadout GetLoadout(CardSeries series)
    {
        for (PCLRuntimeLoadout loadout : Loadouts)
        {
            if (loadout.ID == series.ID)
            {
                return loadout;
            }
        }

        return null;
    }

    public void AddLoadout(PCLRuntimeLoadout loadout)
    {
        Loadouts.add(loadout);

        Log("Adding series: " + loadout.Loadout.Name);
    }

    public void AddAllLoadouts()
    {
        Loadouts.clear();

        for (PCLLoadout loadout : GR.PCL.Data.BaseLoadouts)
        {
            PCLRuntimeLoadout r = PCLRuntimeLoadout.TryCreate(loadout);
            if (r != null)
            {
                Loadouts.add(r);
            }
        }

        FullLog("ADD ALL SERIES");
    }

    public void Reset()
    {
        FullLog("RESETTING...");

        ImportBaseData(null);
        Loadouts.clear();
        BannedCards.clear();
        StartingSeries = new _FakeLoadout();
        loadouts.clear();
        startingLoadout = -1;
        Validate();
    }

    @Override
    public PCLDungeonData onSave()
    {
        loadouts.clear();

        for (PCLRuntimeLoadout loadout : Loadouts)
        {
            final AnimatorLoadoutProxy proxy = new AnimatorLoadoutProxy();
            proxy.id = loadout.ID;
            proxy.isBeta = loadout.IsBeta;
            proxy.bonus = loadout.bonus;
            loadouts.add(proxy);
        }

        if (StartingSeries.ID > 0)
        {
            startingLoadout = StartingSeries.ID;
        }
        else
        {
            startingLoadout = GR.PCL.Data.SelectedLoadout.ID;
        }

        Validate();

        FullLog("ON SAVE");


        return this;
    }

    @Override
    public void onLoad(PCLDungeonData data)
    {
        ImportBaseData(data);
        Loadouts.clear();
        BannedCards.clear();

        if (data != null)
        {
            BannedCards.addAll(data.BannedCards);
            StartingSeries = GR.PCL.Data.GetBaseLoadout(data.startingLoadout);

            if (StartingSeries == null && GR.PCL.Config.DisplayBetaSeries.Get())
            {
                StartingSeries = GR.PCL.Data.GetBetaLoadout(data.startingLoadout);
            }

            for (AnimatorLoadoutProxy proxy : data.loadouts)
            {
                final PCLRuntimeLoadout loadout = PCLRuntimeLoadout.TryCreate(GR.PCL.Data.GetLoadout(proxy.id, proxy.isBeta));
                if (loadout != null)
                {
                    loadout.bonus = proxy.bonus;
                    loadout.BuildCard();
                    Loadouts.add(loadout);
                }
            }
        }

        if (StartingSeries == null)
        {
            StartingSeries = GR.PCL.Data.SelectedLoadout;
        }
        Validate();

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
        Loadouts.clear();

        // The series may not be immediately initialized if the user starts a fresh save
        if (GR.PCL.Config.SelectedSeries.Get() == null || GR.PCL.Config.SelectedSeries.Get().size() < MINIMUM_SERIES) {
            GR.PCL.Config.SelectedSeries.Set(PCLJUtils.Map(GR.PCL.Data.GetEveryLoadout(), loadout -> loadout.Series), true);
        }

        // Always include the selected loadout. If for some reason none exists, assign one at random
        if (GR.PCL.Data.SelectedLoadout == null) {
            GR.PCL.Data.SelectedLoadout = PCLJUtils.Random(PCLJUtils.Filter(GR.PCL.Data.GetEveryLoadout(), loadout -> GR.PCL.GetUnlockLevel() >= loadout.UnlockLevel));
        }
        if (GR.PCL.Data.SelectedLoadout != null) {
            final PCLRuntimeLoadout rloadout = PCLRuntimeLoadout.TryCreate(GR.PCL.Data.SelectedLoadout);
            GR.PCL.Dungeon.AddLoadout(rloadout);
            if (GR.PCL.Data.SelectedLoadout.IsBeta)
            {
                Settings.seedSet = true;
            }
        }

        RandomizedList<PCLLoadout> rList = new RandomizedList<>(GR.PCL.Data.GetEveryLoadout());
        while (Loadouts.size() < GR.PCL.Config.SeriesSize.Get() && rList.Size() > 0) {
            PCLLoadout loadout = rList.Retrieve(GetRNG(), true);
            if ((GR.PCL.Data.SelectedLoadout == null || !GR.PCL.Data.SelectedLoadout.Series.equals(loadout.Series)) && GR.PCL.Config.SelectedSeries.Get().contains(loadout.Series)) {
                if (loadout.IsBeta)
                {
                    // Do not unlock trophies or ascension
                    Settings.seedSet = true;
                }

                final PCLRuntimeLoadout rloadout = PCLRuntimeLoadout.TryCreate(loadout);
                // Series must be unlocked to be present in-game
                if (rloadout != null && !rloadout.isLocked) {
                    GR.PCL.Dungeon.AddLoadout(rloadout);
                }
            }
        }

        if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass) && PCLGameUtilities.IsNormalRun(false) && Settings.seed != null)
        {
            GR.PCL.Config.LastSeed.Set(Settings.seed.toString(), true);
        }


        final AbstractPlayer player = CombatStats.RefreshPlayer();
        if (player.chosenClass != GR.PCL.PlayerClass)
        {
            AbstractDungeon.srcColorlessCardPool.group.removeIf(PCLCard.class::isInstance);
            AbstractDungeon.colorlessCardPool.group.removeIf(PCLCard.class::isInstance);
            PCLEvent.UpdateEvents(false);
            PCLRelic.UpdateRelics(false);
            return;
        }

        PCLEvent.UpdateEvents(true);
        PCLRelic.UpdateRelics(true);

        if (startGame && Settings.isStandardRun())
        {
            GR.PCL.Data.SaveTrophies(true);
        }

        if (Loadouts.isEmpty())
        {
            return;
        }

        final ArrayList<CardGroup> groups = new ArrayList<>();
        groups.addAll(PCLGameUtilities.GetCardPools());
        groups.addAll(PCLGameUtilities.GetSourceCardPools());
        for (CardGroup group : groups)
        {
            group.group.removeIf(card ->
            {
                if (card.color == AbstractCard.CardColor.COLORLESS || card.color == AbstractCard.CardColor.CURSE)
                {
                    return !(card instanceof PCLCardBase) && !(card instanceof CustomCard);
                }
                else if (card.color != GR.PCL.CardColor)
                {
                    return false;
                }
                else if (!BannedCards.contains(card.cardID))
                {
                    for (PCLRuntimeLoadout loadout : Loadouts)
                    {
                        if (loadout.GetCardPoolInPlay().containsKey(card.cardID))
                        {
                            return false;
                        }
                    }
                }

                return true;
            });
        }

        for (AbstractCard card : player.masterDeck.group)
        {
            if (card instanceof OnCardPoolChangedListener)
            {
                ((OnCardPoolChangedListener) card).OnCardPoolChanged();
            }

            RemoveExtraCopies(card);
        }


        if (startGame) {
            for (int i = 0; i < (GR.PCL.Data.SelectedLoadout != null ? GR.PCL.Data.SelectedLoadout.GetCommonUpgrades() : 0); i++) {
                PCLGameEffects.TopLevelQueue.Add(new PermanentUpgradeEffect()).SetFilter(c -> AbstractCard.CardRarity.COMMON.equals(c.rarity));
            }

            player.potionSlots += (GR.PCL.Data.SelectedLoadout != null ? GR.PCL.Data.SelectedLoadout.GetPotionSlots() : 0);
            while (player.potions.size() > player.potionSlots && player.potions.get(player.potions.size() - 1) instanceof PotionSlot) {
                player.potions.remove(player.potions.size() - 1);
            }
            while (player.potionSlots > player.potions.size()) {
                player.potions.add(new PotionSlot(player.potions.size() - 1));
            }
            player.adjustPotionPositions();
        }
    }

    public boolean TryObtainCard(AbstractCard card)
    {
        boolean canAdd = !(card instanceof OnAddToDeckListener) || ((OnAddToDeckListener) card).OnAddToDeck(card);

        for (AbstractRelic relic : AbstractDungeon.player.relics)
        {
            if (relic instanceof OnAddToDeckListener)
            {
                canAdd &= ((OnAddToDeckListener) relic).OnAddToDeck(card);
            }
        }

        return canAdd;
    }

    public void OnCardObtained(AbstractCard card)
    {
        RemoveExtraCopies(card);

        if (card.tags.contains(GR.Enums.CardTags.UNIQUE))
        {
            AbstractCard first = null;
            final ArrayList<AbstractCard> toRemove = new ArrayList<>();
            final ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
            for (AbstractCard c : cards)
            {
                if (c.cardID.equals(card.cardID))
                {
                    if (first == null)
                    {
                        first = c;
                    }
                    else
                    {
                        toRemove.add(c);
                        for (int i = 0; i <= c.timesUpgraded; i++)
                        {
                            first.upgrade();
                        }
                    }
                }
            }

            for (AbstractCard c : toRemove)
            {
                cards.remove(c);
            }

            if (first != null && toRemove.size() > 0 && PCLGameEffects.TopLevelQueue.Count() < 5)
            {
                PCLGameEffects.TopLevelQueue.Add(new UpgradeShineEffect((float) Settings.WIDTH / 4f, (float) Settings.HEIGHT / 2f));
                PCLGameEffects.TopLevelQueue.ShowCardBriefly(first.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4f, (float) Settings.HEIGHT / 2f);
            }
        }
    }

    public void Ban(String cardID)
    {
        final AbstractCard card = CardLibrary.getCard(cardID);
        if (card == null)
        {
            return;
        }

        RemoveCardFromPools(card);
        BannedCards.add(card.cardID);
        Log("Banned " + card.cardID + ", Total: " + BannedCards.size());
    }

    public boolean CanObtainCopy(AbstractCard card) {
        final PCLCard pclCard = (PCLCard) JUtils.SafeCast(card, PCLCard.class);
        if (!Settings.isEndless && pclCard != null && pclCard.cardData.MaxCopies > 0) {
            return PCLGameUtilities.GetAllCopies(pclCard.cardID, AbstractDungeon.player.masterDeck).size() < pclCard.cardData.MaxCopies;
        }
        return true;
    }

    private void RemoveExtraCopies(AbstractCard card)
    {
        final PCLCard pclCard = PCLJUtils.SafeCast(card, PCLCard.class);
        if (!Settings.isEndless && pclCard != null && pclCard.cardData.MaxCopies > 0)
        {
            final int copies = PCLGameUtilities.GetAllCopies(pclCard.cardID, AbstractDungeon.player.masterDeck).size();
            if (copies >= pclCard.cardData.MaxCopies)
            {
                RemoveCardFromPools(pclCard);
            }
        }
    }

    private void RemoveCardFromPools(AbstractCard card)
    {
        final AbstractCard.CardRarity rarity = card.color == AbstractCard.CardColor.COLORLESS ? null : card.rarity;
        final CardGroup srcPool = PCLGameUtilities.GetCardPoolSource(rarity);
        if (srcPool != null)
        {
            srcPool.removeCard(card.cardID);
        }
        final CardGroup pool = PCLGameUtilities.GetCardPool(rarity);
        if (pool != null)
        {
            pool.removeCard(card.cardID);
        }
    }

    public void RemoveRelic(String relicID)
    {
        final ArrayList<String> pool = PCLGameUtilities.GetRelicPool(RelicLibrary.getRelic(relicID).tier);
        if (pool != null)
        {
            pool.remove(relicID);
        }
    }

    public void AddRelic(String relicID, AbstractRelic.RelicTier tier)
    {
        if (!AbstractDungeon.player.hasRelic(relicID))
        {
            final ArrayList<String> pool = PCLGameUtilities.GetRelicPool(tier);
            if (pool != null && pool.size() > 0 && !pool.contains(relicID))
            {
                Random rng = AbstractDungeon.relicRng;
                if (rng == null)
                {
                    rng = GR.PCL.Dungeon.GetRNG();
                }

                pool.add(rng.random(pool.size() - 1), relicID);
            }
        }
    }

    protected void ImportBaseData(PCLDungeonData data)
    {
        if (data != null)
        {
            EventLog = new HashMap<>(data.EventLog);
            EnteredUnnamedReign = data.EnteredUnnamedReign;
            RNGCounter = data.RNGCounter;
            IsCheating = data.IsCheating;
            LongestMatchCombo = data.LongestMatchCombo;
            rng = data.rng;
        }
        else
        {
            EventLog = new HashMap<>();
            EnteredUnnamedReign = false;
            LongestMatchCombo = 0;
            RNGCounter = 0;
            IsCheating = false;
            rng = null;
        }
    }

    protected void Validate()
    {
        if (EventLog == null)
        {
            EventLog = new HashMap<>();
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

        if (LongestMatchCombo == null) {
            LongestMatchCombo = 0;
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
        if (includeRares && (roll <= 4 || (!ignoreCurrentRoom && PCLGameUtilities.GetCurrentRoom() instanceof MonsterRoomBoss)))
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

    private void Log(String message)
    {
        PCLJUtils.LogInfo(this, message);
    }

    private void FullLog(String message)
    {
        PCLJUtils.LogInfo(this, message);
        PCLJUtils.LogInfo(this, "[Transient  Data] Starting Series: " + StartingSeries.Name + ", Series Count: " + Loadouts.size());
        PCLJUtils.LogInfo(this, "[Persistent Data] Starting Series: " + startingLoadout + ", Series Count: " + loadouts.size());
    }

    protected static class AnimatorLoadoutProxy
    {
        public int id;
        public int bonus;
        public boolean isBeta;
    }
}
