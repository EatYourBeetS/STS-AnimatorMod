package eatyourbeets.resources.animator;

import basemod.BaseMod;
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
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.animator.auras.Aura;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.interfaces.listeners.OnAddedToDeckListener;
import eatyourbeets.interfaces.listeners.OnCardPoolChangedListener;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

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

    public AnimatorRuntimeLoadout GetSeries(Synergy synergy)
    {
        for (AnimatorRuntimeLoadout series : Series)
        {
            if (series.ID == synergy.ID)
            {
                return series;
            }
        }

        return null;
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
        FullLog("RESETTING...");

        Series.clear();
        BannedCards.clear();
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
            surrogate.bonus = loadout.bonus;
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

            if (StartingSeries == null && GR.Animator.Config.DisplayBetaSeries())
            {
                StartingSeries = GR.Animator.Data.GetBetaLoadout(data.startingLoadout);
            }

            for (AnimatorLoadoutProxy proxy : data.loadouts)
            {
                AnimatorRuntimeLoadout loadout = AnimatorRuntimeLoadout.TryCreate(GR.Animator.Data.GetLoadout(proxy.id, proxy.isBeta));
                if (loadout != null)
                {
                    if (proxy.promoted)
                    {
                        loadout.Promote();
                    }

                    loadout.bonus = proxy.bonus;
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
        final AbstractPlayer player = CombatStats.RefreshPlayer();
        if (player.chosenClass != GR.Animator.PlayerClass)
        {
            AbstractDungeon.srcColorlessCardPool.group.removeIf(AnimatorCard.class::isInstance);
            AbstractDungeon.colorlessCardPool.group.removeIf(AnimatorCard.class::isInstance);
            EYBEvent.UpdateEvents(false);
            AnimatorRelic.UpdateRelics(false);

            return;
        }

        EYBEvent.UpdateEvents(true);
        AnimatorRelic.UpdateRelics(true);

        if (startGame && Settings.isStandardRun())
        {
            GR.Animator.Data.SaveTrophies(true);
        }

        if (Series.isEmpty())
        {
            return;
        }

        ArrayList<CardGroup> colorless = new ArrayList<>();
        colorless.add(AbstractDungeon.colorlessCardPool);
        colorless.add(AbstractDungeon.srcColorlessCardPool);

        for (CardGroup group : colorless)
        {
            group.group.removeIf(card -> !(card instanceof EYBCard) || card instanceof Aura);
        }

        ArrayList<CardGroup> groups = new ArrayList<>();
        groups.add(AbstractDungeon.commonCardPool);
        groups.add(AbstractDungeon.srcCommonCardPool);
        groups.add(AbstractDungeon.uncommonCardPool);
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

        for (AbstractCard card : player.masterDeck.group)
        {
            if (card instanceof OnCardPoolChangedListener)
            {
                ((OnCardPoolChangedListener) card).OnCardPoolChanged();
            }

            RemoveExtraCopies(card);
        }
    }

    public void OnCardObtained(AbstractCard card)
    {
        AbstractPlayer p = AbstractDungeon.player;

        if (card instanceof OnAddedToDeckListener)
        {
            ((OnAddedToDeckListener) card).OnAddedToDeck(card);
        }
        for (AbstractRelic relic : p.relics)
        {
            if (relic instanceof OnAddedToDeckListener)
            {
                ((OnAddedToDeckListener)relic).OnAddedToDeck(card);
            }
        }


        RemoveExtraCopies(card);

        if (card.tags.contains(GR.Enums.CardTags.UNIQUE))
        {
            AbstractCard first = null;

            ArrayList<AbstractCard> toRemove = new ArrayList<>();
            ArrayList<AbstractCard> cards = p.masterDeck.group;
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

            if (first != null && toRemove.size() > 0 && GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect((float) Settings.WIDTH / 4f, (float) Settings.HEIGHT / 2f));
                GameEffects.TopLevelQueue.ShowCardBriefly(first.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4f, (float) Settings.HEIGHT / 2f);
            }
        }
    }

    public void Ban(String cardID)
    {
        AbstractCard card = CardLibrary.getCard(cardID);
        if (card == null)
        {
            return;
        }

        CardGroup srcPool = GameUtilities.GetCardPoolSource(card.rarity, card.color);
        if (srcPool != null)
        {
            srcPool.removeCard(card.cardID);
        }

        CardGroup pool = GameUtilities.GetCardPool(card.rarity, card.color);
        if (pool != null)
        {
            pool.removeCard(card.cardID);
        }

        BannedCards.add(card.cardID);

        Log("Banned " + card.cardID + ", Total: " + BannedCards.size());
    }

    private void RemoveExtraCopies(AbstractCard card)
    {
        EYBCard eybCard = JUtils.SafeCast(card, EYBCard.class);
        if (eybCard != null)
        {
            if (eybCard.cardData.MaxCopies > 0)
            {
                int copies = GameUtilities.GetAllCopies(eybCard.cardID, AbstractDungeon.player.masterDeck).size();
                if (copies > eybCard.cardData.MaxCopies)
                {
                    RemoveCardFromPools(eybCard);
                }
            }
        }
    }

    private void RemoveCardFromPools(AbstractCard card)
    {
        if (card.color == AbstractCard.CardColor.COLORLESS)
        {
            AbstractDungeon.colorlessCardPool.removeCard(card.cardID);
            AbstractDungeon.srcColorlessCardPool.removeCard(card.cardID);
            return;
        }

        switch (card.rarity)
        {
            case BASIC:
            case SPECIAL:
            case CURSE:
            {
                JUtils.LogWarning(this, "Wrong card rarity for RemoveCardFromPools(), " + card.cardID);
                break;
            }

            case COMMON:
            {
                AbstractDungeon.commonCardPool.removeCard(card.cardID);
                AbstractDungeon.srcCommonCardPool.removeCard(card.cardID);
                break;
            }

            case UNCOMMON:
            {
                AbstractDungeon.uncommonCardPool.removeCard(card.cardID);
                AbstractDungeon.srcUncommonCardPool.removeCard(card.cardID);
                break;
            }

            case RARE:
            {
                AbstractDungeon.rareCardPool.removeCard(card.cardID);
                AbstractDungeon.srcRareCardPool.removeCard(card.cardID);
                break;
            }
        }
    }

    public void RemoveRelic(String relicID)
    {
        ArrayList<String> pool = GameUtilities.GetRelicPool(RelicLibrary.getRelic(relicID).tier);
        if (pool != null)
        {
            pool.remove(relicID);
        }
    }

    public void AddRelic(String relicID, AbstractRelic.RelicTier tier)
    {
        if (!AbstractDungeon.player.hasRelic(relicID))
        {
            ArrayList<String> pool = GameUtilities.GetRelicPool(tier);
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
        JUtils.LogInfo(this, "[Transient  Data] Starting Series: " + StartingSeries.Name + ", Series Count: " + Series.size());
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
