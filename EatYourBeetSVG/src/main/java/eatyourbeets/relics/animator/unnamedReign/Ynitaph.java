package eatyourbeets.relics.animator.unnamedReign;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.stats.RunData;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;
import patches.RelicLibraryPatches;
import patches.screens.RunHistoryScreenPatches;

import java.util.ArrayList;

public class Ynitaph extends AnimatorRelic implements CustomSavable<Integer>, OnEquipUnnamedReignRelicListener
{
    public static final String ID = CreateFullID(Ynitaph.class);
    public static final int MINIMUM_ASCENSION = 13;
    public static final int STAT_BONUS = 1;

    public static boolean CanSpawn()
    {
        return GameUtilities.IsNormalRun(true) && GameUtilities.GetActualAscensionLevel() >= Ynitaph.MINIMUM_ASCENSION;
    }

    public static void TryRestoreFromPreviousRun()
    {
        if ((player != null && player.hasRelic(Ynitaph.ID)) || !GameUtilities.IsNormalRun(true))
        {
            return;
        }

        final AbstractPlayer player = CombatStats.RefreshPlayer();
        final int minAscension = Math.max(GameUtilities.GetActualAscensionLevel() - 1, MINIMUM_ASCENSION);
        final RunData lastRun = RunHistoryScreenPatches.GetLastRunData(player.chosenClass, minAscension);
        if (lastRun != null && lastRun.victory && lastRun.ascension_level <= (minAscension + 1))
        {
            for (String relicID : lastRun.relics)
            {
                if (relicID.startsWith(ID))
                {
                    final Ynitaph relic = JUtils.SafeCast(RelicLibraryPatches.GetRelic(relicID), Ynitaph.class);
                    if (relic != null)
                    {
                        relic.obtainable = true;
                        relic.isAnimating = false;
                        relic.isObtained = true;
                        relic.isDone = true;
                        player.relics.add(relic);
                        player.reorganizeRelics();
                        return;
                    }
                }
            }
        }
    }

    protected int level = 1;
    protected boolean obtainable;

    public Ynitaph()
    {
        this(1, false);
    }

    public Ynitaph(int level, boolean obtainable)
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);

        this.obtainable = obtainable;
        this.mainTooltip.title += FormatDescription(0, MINIMUM_ASCENSION);

        SetLevel(level);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (!obtainable)
        {
            LoseRelic();
        }
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {

    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        if (GameUtilities.InEliteRoom())
        {
            GameUtilities.ApplyPowerInstantly(player, PowerHelper.Strength, STAT_BONUS);
            if (level > 1)
            {
                GameUtilities.ApplyPowerInstantly(player, PowerHelper.Dexterity, STAT_BONUS);
                if (level > 2)
                {
                    GameUtilities.ApplyPowerInstantly(player, player.orbs.isEmpty() ? PowerHelper.Metallicize : PowerHelper.Focus, STAT_BONUS);
                }
            }

            this.flash();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(1, MINIMUM_ASCENSION) + FormatDescription(level + 1, STAT_BONUS);
    }

    @Override
    public Integer onSave()
    {
        return level;
    }

    @Override
    public void onLoad(Integer integer)
    {
        if (obtainable = (integer != null))
        {
            SetLevel(integer);
        }
    }

    @Override
    public void obtain()
    {
        if (!obtainable)
        {
            return;
        }

        final ArrayList<AbstractRelic> relics = player.relics;
        for (AbstractRelic abstractRelic : relics)
        {
            final Ynitaph relic = JUtils.SafeCast(abstractRelic, Ynitaph.class);
            if (relic != null)
            {
                relic.SetLevel(relic.level + 1);
                relic.flash();
                return;
            }
        }

        super.obtain();
    }

    public void LoseRelic()
    {
        GameEffects.TopLevelQueue.Callback(() -> player.relics.remove(this));
    }

    public void SetLevel(int level)
    {
        this.level = Mathf.Clamp(level, 1, 3);
        this.updateDescription(null);
        this.RefreshTexture();
    }

    public void RefreshTexture()
    {
        setTexture(GR.GetTexture(GR.GetRelicImage((level > 1) ? (relicId + "_" + level) : relicId)));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        Ynitaph copy = (Ynitaph)super.makeCopy();
        copy.obtainable = obtainable;
        copy.SetLevel(level);
        return copy;
    }
}
