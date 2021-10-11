package eatyourbeets.cards.base;

import com.google.gson.annotations.SerializedName;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JUtils;

public class EYBCardMetadataV2
{
    @SerializedName("Series")
    protected String series;
    @SerializedName("Stats")
    protected int[] stats;
    @SerializedName("Stats+")
    protected int[] upgrade;
    @SerializedName("Scaling")
    protected int[] scaling;
    @SerializedName("Affinity")
    protected int[] affinity;
    @SerializedName("StaticPortrait")
    protected Boolean staticPortrait;

    public void Import(AbstractCard c)
    {
        AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
        if (card == null)
        {
            JUtils.LogError(this, "Not an Animator card: " + c.cardID);
            return;
        }

        if (series != null)
        {
            card.SetSeries(CardSeries.GetByName(series, false));
        }

        if (stats != null && stats.length > 0)
        {
            card.cost = card.costForTurn = stats[0];

            switch (stats.length)
            {
                case 2:
                    card.Initialize(stats[1], 0);
                    break;

                case 3:
                    card.Initialize(stats[1], stats[2]);
                    break;

                case 4:
                    card.Initialize(stats[1], stats[2], stats[3]);
                    break;

                case 5:
                    card.Initialize(stats[1], stats[2], stats[3], stats[4]);
                    break;
            }
        }

        if (upgrade != null && upgrade.length > 0)
        {
            card.SetCostUpgrade(upgrade[0]);

            switch (upgrade.length)
            {
                case 2:
                    card.SetUpgrade(upgrade[1], 0);
                    break;

                case 3:
                    card.SetUpgrade(upgrade[1], upgrade[2]);
                    break;

                case 4:
                    card.SetUpgrade(upgrade[1], upgrade[2], upgrade[3]);
                    break;

                case 5:
                    card.SetUpgrade(upgrade[1], upgrade[2], upgrade[3], upgrade[4]);
                    break;
            }
        }

// TODO: export scaling and affinity
//
//        if (scaling != null)
//        {
//            if (affinity.length != 3)
//            {
//                throw new RuntimeException("Card scaling must have 3 elements: " + card.cardID);
//            }
//
//            card.SetScaling(scaling[2], scaling[1], scaling[0]);
//        }
//
//        if (affinity != null)
//        {
//            if (affinity.length != 5)
//            {
//                throw new RuntimeException("Card alignment must have 5 elements: " + card.cardID);
//            }
//
//            card.SetAffinity(affinity[0], affinity[1], affinity[2], affinity[3], affinity[4]);
//        }

        if (staticPortrait != null)
        {
            card.cropPortrait = staticPortrait;
        }
    }

    public static EYBCardMetadataV2 Export(AbstractCard toExport, boolean exportSeries)
    {
        AnimatorCard card = JUtils.SafeCast(toExport.makeCopy(), AnimatorCard.class);
        if (card == null)
        {
            return null;
        }

        EYBCardMetadataV2 metadata = new EYBCardMetadataV2();
        metadata.stats = new int[] {card.cost, Math.max(0, card.baseDamage), Math.max(0, card.baseBlock), card.baseMagicNumber, card.baseSecondaryValue};
        metadata.upgrade = new int[] {card.upgrade_cost, card.upgrade_damage, card.upgrade_block, card.upgrade_magicNumber, card.upgrade_secondaryValue};

        if (card.affinities.List.size() > 0 || card.affinities.HasStar())
        {
            metadata.scaling = new int[]
            {
                card.affinities.GetScaling(Affinity.Fire, false),
                card.affinities.GetScaling(Affinity.Air, false),
                card.affinities.GetScaling(Affinity.Water, false),
                card.affinities.GetScaling(Affinity.Earth, false),
                card.affinities.GetScaling(Affinity.Light, false),
                card.affinities.GetScaling(Affinity.Dark, false)
            };
        }

        if (exportSeries && card.series != null)
        {
            metadata.series = card.series.Name;
        }

        metadata.affinity = new int[] {0, 0, 0, 0, 0 };
        for (EYBCardAffinity a : card.affinities.List)
        {
            if (a.type.ID >= 0)
            {
                metadata.affinity[a.type.ID] = a.level;
            }
        }

        if (!card.cropPortrait)
        {
            metadata.staticPortrait = true;
        }

        return metadata;
    }
}
