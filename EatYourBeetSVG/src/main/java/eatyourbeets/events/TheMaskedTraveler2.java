package eatyourbeets.events;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.effects.special.MaskedTravelerTransformCardsEffect;
import eatyourbeets.effects.special.UnnamedRelicEquipEffect;
import eatyourbeets.relics.UnnamedReign.AncientMedallion;
import eatyourbeets.relics.UnnamedReign.TheEgnaroPiece;
import eatyourbeets.relics.UnnamedReign.TheEruzaStone;
import eatyourbeets.relics.UnnamedReign.TheWolleyCore;

import java.util.ArrayList;

public class TheMaskedTraveler2 extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheMaskedTraveler2.class.getSimpleName());

    private final AbstractRelic relic1 = new TheEruzaStone();
    private final AbstractRelic relic2 = new TheWolleyCore();
    private final AbstractRelic relic3 = new TheEgnaroPiece();
    private final AbstractRelic medallion = new AncientMedallion(true);

    private static final int REMOVE_CARDS = 2;
    private static final int OBTAIN_CARDS = 2;
    private static final int UPGRADE_SAME_CARD = 4;

    private final ArrayList<AbstractRelic> startingRelicsCache = new ArrayList<>();

    private int currentHPLoss = 0;

    public TheMaskedTraveler2()
    {
        super(ID, "secretPortal.jpg");

        MapRoomNode node = AbstractDungeon.getCurrMapNode();
        if (node != null && node.room != null)
        {
            node.room.rewardAllowed = false;
        }

        RegisterPhase(-1, this::CreateSpecialPhase, this::HandleSpecialPhase);
        RegisterPhase(1, this::CreatePhase1, this::HandlePhase1);
        RegisterPhase(2, this::CreatePhase2, this::HandlePhase2);
        RegisterPhase(3, this::CreatePhase3, this::HandlePhase3);
        ProgressPhase();
    }

    private void CreatePhase1()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[0], true);
        UpdateDialogOption(0, OPTIONS[1]); // Continue
        UpdateDialogOption(1, OPTIONS[0]); // Leave
    }

    private void HandlePhase1(int button)
    {
        if (button == 0)
        {
            ProgressPhase();
        }
        else
        {
            this.openMap();
        }
    }

    private void CreatePhase2()
    {
        String goldBonus = String.valueOf(UnnamedRelicEquipEffect.CalculateGoldBonus() + 21);

        UpdateBodyText(eventStrings.DESCRIPTIONS[1].replace("{0}", goldBonus), true);
        UpdateDialogOption(0, OPTIONS[2], relic1);
        UpdateDialogOption(1, OPTIONS[2], relic2);
        UpdateDialogOption(2, OPTIONS[2], relic3);
        UpdateDialogOption(3, OPTIONS[0]); // Leave
    }

    private void HandlePhase2(int button)
    {
        for (AbstractRelic relic : AbstractDungeon.player.relics)
        {
            if (relic.tier == AbstractRelic.RelicTier.STARTER)
            {
                startingRelicsCache.add(relic);
            }
        }

        if (button == 0)
        {
            ObtainRelic(relic1);
        }
        else if (button == 1)
        {
            ObtainRelic(relic2);
        }
        else if (button == 2)
        {
            ObtainRelic(relic3);
        }
        else
        {
            this.openMap();
            return;
        }

        ProgressPhase();
    }

    private void CreatePhase3()
    {
        AbstractPlayer p = AbstractDungeon.player;

        String message1 = OPTIONS[3].replace("{0}", String.valueOf(REMOVE_CARDS));
        message1 = message1.replace("{1}", String.valueOf(OBTAIN_CARDS));

        int hpLossPercentage = 8;
        if (p instanceof AnimatorCharacter)
        {
            hpLossPercentage = 16;
        }

        currentHPLoss = (int) Math.ceil((UnnamedRelicEquipEffect.CalculateMaxHealth() / 100.0) * hpLossPercentage);
        String message2 = OPTIONS[4].replace("{0}", String.valueOf(currentHPLoss));

        UpdateBodyText(eventStrings.DESCRIPTIONS[2], true);
        UpdateDialogOption(0, OPTIONS[2], medallion);
        UpdateDialogOption(1, message1);
        UpdateDialogOption(2, message2);
    }

    private void HandlePhase3(int button)
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[2], true);
        if (button == 0)
        {
            ObtainRelic(medallion);
        }
        else if (button == 1)
        {
            AbstractDungeon.effectsQueue.add(new MaskedTravelerTransformCardsEffect(REMOVE_CARDS));
        }
        else if (button == 2)
        {
            AbstractDungeon.player.decreaseMaxHealth(currentHPLoss);

            for (String relicID : AbstractDungeon.player.getLoadout().relics)
            {
                if (!RecoverPreviousRelic(relicID))
                {
                    AbstractRelic relic = RelicLibrary.getRelic(relicID).makeCopy();
                    relic.instantObtain();
                }
            }

            startingRelicsCache.clear();
            //AbstractDungeon.effectsQueue.add(new MaskedTravelerUpgradeSameCardEffect(UPGRADE_SAME_CARD));
        }

        ProgressPhase(-1);
    }

    private void CreateSpecialPhase()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[2], true);
        UpdateDialogOption(0, OPTIONS[0]);
    }

    private void HandleSpecialPhase(int button)
    {
        TheUnnamedReign.EnterDungeon();
    }

    private void ObtainRelic(AbstractRelic relic)
    {
        relic.instantObtain();
        CardCrawlGame.metricData.addRelicObtainData(relic);
        //AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, relic.makeCopy());
    }

    private boolean RecoverPreviousRelic(String relicID)
    {
        AbstractRelic relic = null;
        for (AbstractRelic previousRelic : startingRelicsCache)
        {
            if (relicID.equals(previousRelic.relicId))
            {
                relic = previousRelic;
                break;
            }
        }

        if (relic == null)
        {
            return false;
        }

        if ("Circlet".equals(relic.relicId) && AbstractDungeon.player.hasRelic("Circlet"))
        {
            AbstractRelic circ = AbstractDungeon.player.getRelic("Circlet");
            ++circ.counter;
            circ.flash();
        }
        else
        {
            float START_X = 64.0F * Settings.scale;
            float START_Y = (float) Settings.HEIGHT - 102.0F * Settings.scale;

            relic.playLandingSFX();
            relic.isDone = true;
            relic.isObtained = true;
            relic.currentX = START_X + (float) AbstractDungeon.player.relics.size() * AbstractRelic.PAD_X;
            relic.currentY = START_Y;
            relic.targetX = relic.currentX;
            relic.targetY = relic.currentY;
            relic.flash();
            AbstractDungeon.player.relics.add(relic);
            relic.hb.move(relic.currentX, relic.currentY);
            //relic.onEquip();
            relic.relicTip();
            UnlockTracker.markRelicAsSeen(relic.relicId);
        }

        if (AbstractDungeon.topPanel != null)
        {
            AbstractDungeon.topPanel.adjustRelicHbs();
        }

        return true;
    }
}