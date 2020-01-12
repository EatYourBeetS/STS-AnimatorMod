package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.series.Elsword.Eve;
import eatyourbeets.cards.animator.series.Fate.Saber;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.RoyMustang;
import eatyourbeets.cards.animator.series.GATE.PinaCoLada;
import eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.ChaikaTrabant;
import eatyourbeets.cards.animator.series.Katanagatari.Togame;
import eatyourbeets.cards.animator.series.Konosuba.Megumin;
import eatyourbeets.cards.animator.series.NoGameNoLife.Sora;
import eatyourbeets.cards.animator.series.OnePunchMan.Saitama;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Shinoa;
import eatyourbeets.cards.animator.series.TenseiSlime.Shizu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: this is now useless
public abstract class AbstractPurgingStone extends AnimatorRelic
{
    public static final Map<Synergy, AnimatorCard> mainSeries = new HashMap<>();

    public AbstractPurgingStone(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, imageID, tier, sfx);
    }

    public static void UpdateBannedCards()
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null && player.relics != null)
        {
            for (AbstractRelic relic : player.relics)
            {
                if (relic instanceof AbstractPurgingStone)
                {
                    ((AbstractPurgingStone)relic).UpdateBannedCardsInternal();
                }
            }
        }
    }

    public static Iterable<Synergy> GetAvailableSeries()
    {
        PurgingStone_Series purgingStone = GameUtilities.GetRelic(PurgingStone_Series.ID);
        if (purgingStone != null)
        {
            ArrayList<Synergy> synergies = new ArrayList<>(mainSeries.keySet());
            synergies.removeIf(purgingStone::IsBanned);
            return synergies;
        }
        else
        {
            return mainSeries.keySet();
        }
    }

    public abstract void UpdateBannedCardsInternal();
    public abstract boolean IsBanned(AbstractCard card);

    static
    {
        Create(Synergies.Konosuba, new Megumin());
        Create(Synergies.Elsword, new Eve());
        Create(Synergies.Chaika, new ChaikaTrabant());
        Create(Synergies.Fate, new Saber());
        Create(Synergies.Gate, new PinaCoLada());
        Create(Synergies.FullmetalAlchemist, new RoyMustang());
        Create(Synergies.GoblinSlayer, new GoblinSlayer());
        Create(Synergies.Katanagatari, new Togame());
        Create(Synergies.NoGameNoLife, new Sora());
        Create(Synergies.OnePunchMan, new Saitama());
        Create(Synergies.Overlord, new Ainz());
        Create(Synergies.OwariNoSeraph, new Shinoa());
        Create(Synergies.TenSura, new Shizu());
    }

    private static void Create(Synergy synergy, AnimatorCard base)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(GR.CreateID("Synergy", String.valueOf(synergy.ID)));
        builder.SetImage(base.assetUrl);
        builder.SetProperties(base.type, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE);
        builder.SetText(synergy.Name, "Contains 10 cards from this series.", "");
        builder.SetSynergy(synergy, false);
        mainSeries.put(synergy, builder.Build());
    }
}