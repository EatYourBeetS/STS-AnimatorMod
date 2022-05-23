package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.StartActSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.animator.ultrarare.Azami;
import eatyourbeets.cards.base.*;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.listeners.OnCardPoolChangedListener;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.animator.Destiny;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AnimatorDungeonData implements CustomSavable<AnimatorDungeonData>, StartGameSubscriber, StartActSubscriber
{
    public transient final ArrayList<AnimatorRuntimeLoadout> Loadouts = new ArrayList<>();
    public transient AnimatorLoadout StartingSeries = new _FakeLoadout();
    public transient boolean HasBetaSeries = false;
    public HashSet<String> BannedCards = new HashSet<>();

    protected ArrayList<AnimatorLoadoutProxy> loadouts = new ArrayList<>();
    protected int startingLoadout = -1;
    protected boolean isBeta;

    public static float GetUltraRareChance()
    {
        final Float rate = GR.Common.Dungeon.GetFloat("UR_RATE", null);
        if (rate != null)
        {
            if (rate > 0 && GameUtilities.InGame())
            {
                GR.Common.Dungeon.SetCheating();
            }

            return rate;
        }


        final int currentLevel = GR.Animator.GetUnlockLevel();
        if (currentLevel <= 2 || AbstractDungeon.floorNum < 8 || AbstractDungeon.floorNum > 36)
        {
            return 0;
        }

        float bonus = 4;
        int level = GR.Animator.Data.SpecialTrophies.Trophy1;
        if (level > 0)
        {
            bonus += level * 1.33f / (level + 100f);
        }

        if (GameUtilities.HasRelic(Destiny.ID))
        {
            bonus += 1.6f;
        }

        return bonus;
    }

    public static EYBCardData CreateUltraRare(AbstractPlayer player) {
        HashMap<CardSeries, Integer> possibleSeries = new HashMap<>();
        HashSet<CardSeries> urSeries = new HashSet<>();
        for (AbstractCard c : player.masterDeck.group)
        {
            CardSeries s = GameUtilities.GetSeries(c);
            if (s != null) {
                possibleSeries.merge(s, 1, Integer::sum);
                if (c instanceof AnimatorCard_UltraRare) {
                    urSeries.add(s);
                }
            }
        }
        WeightedList<CardSeries> weighted = new WeightedList<>();
        for (Map.Entry<CardSeries, Integer> entry : possibleSeries.entrySet()) {
            if (!urSeries.contains(entry.getKey())) {
                weighted.Add(entry.getKey(), entry.getValue());
            }
        }

        CardSeries selected = weighted.Retrieve(AbstractDungeon.cardRng);
        return AnimatorCard_UltraRare.GetCardData(GR.Animator.Data.GetLoadout(selected));
    }

    public static AnimatorDungeonData Register(String id)
    {
        final AnimatorDungeonData data = new AnimatorDungeonData();
        BaseMod.addSaveField(id, data);
        BaseMod.subscribe(data);
        return data;
    }

    public AnimatorRuntimeLoadout GetLoadout(CardSeries series)
    {
        if (series != null)
        {
            for (AnimatorRuntimeLoadout loadout : Loadouts)
            {
                if (loadout.ID == series.ID)
                {
                    return loadout;
                }
            }
        }

        return null;
    }

    public void AddLoadout(AnimatorRuntimeLoadout loadout)
    {
        Loadouts.add(loadout);

        Log("Adding series: " + loadout.Loadout.Name);
    }

    public void AddAllLoadouts()
    {
        Loadouts.clear();

        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            AnimatorRuntimeLoadout r = AnimatorRuntimeLoadout.TryCreate(loadout);
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

        Loadouts.clear();
        BannedCards.clear();
        StartingSeries = new _FakeLoadout();
        loadouts.clear();
        startingLoadout = -1;
        isBeta = GR.Animator.Config.DisplayBetaSeries.Get();
    }

    @Override
    public AnimatorDungeonData onSave()
    {
        loadouts.clear();
        isBeta = HasBetaSeries;

        for (AnimatorRuntimeLoadout loadout : Loadouts)
        {
            final AnimatorLoadoutProxy proxy = new AnimatorLoadoutProxy();
            proxy.id = loadout.ID;
            proxy.isBeta = loadout.IsBeta;
            proxy.promoted = loadout.promoted;
            proxy.bonus = loadout.bonus;
            loadouts.add(proxy);
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
        Loadouts.clear();
        BannedCards.clear();

        if (data != null)
        {
            BannedCards.addAll(data.BannedCards);
            StartingSeries = GR.Animator.Data.GetBaseLoadout(data.startingLoadout);
            HasBetaSeries = data.isBeta;

            if (StartingSeries == null && GR.Animator.Config.DisplayBetaSeries.Get())
            {
                StartingSeries = GR.Animator.Data.GetBetaLoadout(data.startingLoadout);
            }

            for (AnimatorLoadoutProxy proxy : data.loadouts)
            {
                final AnimatorRuntimeLoadout loadout = AnimatorRuntimeLoadout.TryCreate(GR.Animator.Data.GetLoadout(proxy.id, proxy.isBeta));
                if (loadout != null)
                {
                    if (proxy.promoted)
                    {
                        loadout.Promote();
                    }

                    loadout.bonus = proxy.bonus;
                    loadout.BuildCard();
                    Loadouts.add(loadout);
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

        if (AbstractDungeon.actNum == 1 && AbstractDungeon.floorNum == 0)
        {
            CombatStats.OnNewRunStart(true);
        }
    }

    @Override
    public void receiveStartGame()
    {
        FullLog("ON GAME START");
        InitializeCardPool(true);

        if (AbstractDungeon.actNum == 1 && AbstractDungeon.floorNum == 0)
        {
            CombatStats.OnNewRunStart(false);
        }
        GameUtilities.GetAscensionData(true).OnLoadGame();
    }

    public void InitializeCardPool(boolean startGame)
    {
        final AbstractPlayer player = CombatStats.RefreshPlayer();
        if (player.chosenClass != GR.Animator.PlayerClass)
        {
            AbstractDungeon.srcColorlessCardPool.group.removeIf(AnimatorCard.class::isInstance);
            AbstractDungeon.colorlessCardPool.group.removeIf(AnimatorCard.class::isInstance);
            AbstractDungeon.curseCardPool.group.removeIf(AnimatorCard.class::isInstance);
            EYBEvent.UpdateEvents(false);
            AnimatorRelic.UpdateRelics(false);
        }

        if (!GameUtilities.IsEYBPlayerClass())
        {
            return;
        }

        if (player.chosenClass == GR.Animator.PlayerClass) // TODO: Call AnimatorDungeonData and UnnamedDungeonData from CommonDungeonData
        {
            EYBEvent.UpdateEvents(true);
            AnimatorRelic.UpdateRelics(true);

            if (startGame && Settings.isStandardRun())
            {
                GR.Animator.Data.SaveTrophies(true);
            }

            if (Loadouts.isEmpty())
            {
                return;
            }

            final ArrayList<CardGroup> groups = new ArrayList<>();
            groups.addAll(GameUtilities.GetCardPools());
            groups.addAll(GameUtilities.GetSourceCardPools());
            for (CardGroup group : groups)
            {
                group.group.removeIf(card ->
                {
                    if (card.color == AbstractCard.CardColor.CURSE || card.color == AbstractCard.CardColor.COLORLESS)
                    {
                        return !(card instanceof EYBCardBase) && !(card instanceof CustomCard);
                    }
                    else if (card.color != GR.Animator.CardColor)
                    {
                        return false;
                    }
                    else if (!BannedCards.contains(card.cardID))
                    {
                        for (AnimatorRuntimeLoadout loadout : Loadouts)
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

        for (AbstractCard card : player.masterDeck.group)
        {
            if (card instanceof OnCardPoolChangedListener)
            {
                ((OnCardPoolChangedListener) card).OnCardPoolChanged();
            }

            RemoveExtraCopies(card);
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
        final int maxCopies = RemoveExtraCopies(card);
        if (maxCopies > 0)
        {
            int copies = 0;
            AbstractCard first = null;
            final ArrayList<AbstractCard> toRemove = new ArrayList<>();
            final ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
            for (AbstractCard c : cards)
            {
                if (c.cardID.equals(card.cardID))
                {
                    copies += 1;

                    if (copies <= maxCopies)
                    {
                        if (first == null || (!c.hasTag(EYBCard.UNIQUE) && !c.upgraded))
                        {
                            first = c;
                        }
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

            if (first != null && toRemove.size() > 0 && GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect((float) Settings.WIDTH / 4f, (float) Settings.HEIGHT / 2f));
                GameEffects.TopLevelQueue.ShowCardBriefly(first.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4f, (float) Settings.HEIGHT / 2f);
            }
        }
    }

    public void OnRelicObtained(AbstractRelic relic)
    {
        if (Sozu.ID.equals(relic.relicId))
        {
            RemoveRelicFromCurrentRun(WhiteBeast.ID);
            RemoveRelicFromCurrentRun(PotionBelt.ID);
            RemoveRelicFromCurrentRun(ToyOrnithopter.ID);
        }
        else if (Ectoplasm.ID.equals(relic.relicId))
        {
            RemoveRelicFromCurrentRun(OldCoin.ID);
            RemoveRelicFromCurrentRun(MawBank.ID);
            RemoveRelicFromCurrentRun(CeramicFish.ID);
        }
        else if (CoffeeDripper.ID.equals(relic.relicId))
        {
            RemoveRelicFromCurrentRun(RegalPillow.ID);
            RemoveRelicFromCurrentRun(DreamCatcher.ID);
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

    public boolean CanObtainCopy(AbstractCard card)
    {
        final EYBCard eybCard = JUtils.SafeCast(card, EYBCard.class);
        if (!Settings.isEndless && eybCard != null && eybCard.cardData.MaxCopies > 0)
        {
            return eybCard.cardData.GetTotalCopies(AbstractDungeon.player.masterDeck) < eybCard.cardData.MaxCopies;
        }

        return true;
    }

    private int RemoveExtraCopies(AbstractCard card)
    {
        int maxCopies = 0;
        final EYBCard eybCard = JUtils.SafeCast(card, EYBCard.class);
        if (!Settings.isEndless && eybCard != null && eybCard.cardData.MaxCopies > 0)
        {
            if (eybCard.cardData.GetTotalCopies(AbstractDungeon.player.masterDeck) >= eybCard.cardData.MaxCopies)
            {
                RemoveCardFromPools(eybCard);
            }

            maxCopies = eybCard.cardData.MaxCopies;
        }

        return card.hasTag(GR.Enums.CardTags.UNIQUE) ? 1 : maxCopies;
    }

    private void RemoveCardFromPools(AbstractCard card)
    {
        final AbstractCard.CardRarity rarity = card.color == AbstractCard.CardColor.COLORLESS ? null : card.rarity;
        final CardGroup srcPool = GameUtilities.GetCardPoolSource(rarity);
        if (srcPool != null)
        {
            srcPool.removeCard(card.cardID);
        }
        final CardGroup pool = GameUtilities.GetCardPool(rarity);
        if (pool != null)
        {
            pool.removeCard(card.cardID);
        }
    }

    public void RemoveRelic(String relicID)
    {
        final ArrayList<String> pool = GameUtilities.GetRelicPool(RelicLibrary.getRelic(relicID).tier);
        if (pool != null)
        {
            pool.remove(relicID);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void RemoveRelicFromCurrentRun(String relicID)
    {
        GameUtilities.GetRelicPool(AbstractRelic.RelicTier.COMMON).remove(relicID);
        GameUtilities.GetRelicPool(AbstractRelic.RelicTier.UNCOMMON).remove(relicID);
        GameUtilities.GetRelicPool(AbstractRelic.RelicTier.RARE).remove(relicID);
        GameUtilities.GetRelicPool(AbstractRelic.RelicTier.SHOP).remove(relicID);
    }

    public void AddRelic(String relicID, AbstractRelic.RelicTier tier)
    {
        if (!AbstractDungeon.player.hasRelic(relicID))
        {
            final ArrayList<String> pool = GameUtilities.GetRelicPool(tier);
            if (pool != null && pool.size() > 0 && !pool.contains(relicID))
            {
                Random rng = AbstractDungeon.relicRng;
                if (rng == null)
                {
                    rng = GR.Common.Dungeon.GetRNG();
                }

                pool.add(rng.random(pool.size() - 1), relicID);
            }
        }
    }

    private void Log(String message)
    {
        JUtils.LogInfo(this, message);
    }

    private void FullLog(String message)
    {
        JUtils.LogInfo(this, message);
        JUtils.LogInfo(this, "[Transient  Data] Starting Series: " + StartingSeries.Name + ", Series Count: " + Loadouts.size());
        JUtils.LogInfo(this, "[Persistent Data] Starting Series: " + startingLoadout + ", Series Count: " + loadouts.size());
    }

    protected static class AnimatorLoadoutProxy
    {
        public int id;
        public int bonus;
        public boolean isBeta;
        public boolean promoted;
    }
}
