package eatyourbeets.ui.animator.cardReward;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.animator.ultrarare.*;
import eatyourbeets.relics.animator.CursedGlyph;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: Change this to give a random bonus based on the card series
public class BundledRelicProvider
{
    private static final Map<String, BundledRelic> bundledRelicsPool = new HashMap<>();
    private static final ArrayList<BundledRelic> bundledRelics = new ArrayList<>();

    private static MapRoomNode lastNode = null;

    public static void SetupBundledRelics(BundledRelicContainer bundle, RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        MapRoomNode mapNode = AbstractDungeon.currMapNode;
        if (lastNode != mapNode)
        {
            lastNode = mapNode;
            bundledRelics.clear();
            JavaUtilities.Logger.info("Clearing Bundles");
        }

        bundle.rewardItem = rItem;
        bundle.bundledRelics.clear();

        for (AbstractCard c : cards)
        {
            BundledRelic bundledRelic = GetBundle(c.cardID);
            if (bundledRelic != null)
            {
                bundle.bundledRelics.add(bundledRelic);
            }
        }
    }

    private static BundledRelic GetBundle(String cardID)
    {
        for (BundledRelic relic : bundledRelics)
        {
            if (relic.cardID.equals(cardID))
            {
                return relic;
            }
        }

        BundledRelic relic = bundledRelicsPool.get(cardID);
        if (relic != null)
        {
            relic = relic.Clone(AbstractDungeon.relicRng.random(99f));
            //bundledRelics.add(relic);
            bundledRelics.add(relic);
        }
        else
        {
            JavaUtilities.Logger.info("Key not found: " + cardID);
        }

        return relic;
    }

    private static void AddBundle(String cardID, String relicID, AbstractRelic.RelicTier tier, float chance)
    {
        bundledRelicsPool.put(cardID, new BundledRelic(cardID, relicID, tier, chance));
    }

    final static float R1 = 20.5f;
    final static float R2 = 21f;
    final static float R3 = 21.5f;
    final static float R4 = 22f;
    final static float R5 = 22.5f;
    final static float R6 = 23f;

    static
    {
        AddElsword();
        AddFate();
        AddFullmetalAlchemist();
        AddGATE();
        AddGoblinSlayer();
        AddHitsugiNoChaika();
        AddKatanagatari();
        AddKonosuba();
        AddNoGameNoLife();
        AddOverlord();
        AddOwariNoSeraph();
        AddTenSura();
        AddOnePunchMan();
    }

    private static void AddElsword()
    {
        AddBundle(Rose.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddFate()
    {
        AddBundle(JeanneDArc.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddFullmetalAlchemist()
    {
        AddBundle(Truth.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddGATE()
    {
        AddBundle(Giselle.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddGoblinSlayer()
    {
        AddBundle(Hero.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddHitsugiNoChaika()
    {
        AddBundle(NivaLada.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddKatanagatari()
    {
        AddBundle(ShikizakiKiki.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddKonosuba()
    {
        AddBundle(Chomusuke.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddNoGameNoLife()
    {
        AddBundle(Azriel.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddOverlord()
    {
        AddBundle(SirTouchMe.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddOwariNoSeraph()
    {
        AddBundle(HiiragiTenri.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddOnePunchMan()
    {
        AddBundle(SeriousSaitama.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }

    private static void AddTenSura()
    {
        AddBundle(Veldora.DATA.ID, CursedGlyph.ID, AbstractRelic.RelicTier.SPECIAL, 100);
    }
}