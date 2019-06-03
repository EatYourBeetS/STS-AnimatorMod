package eatyourbeets.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.relics.AncientMedallion;
import eatyourbeets.relics.TheEgnaroPiece;
import eatyourbeets.relics.TheEruzaStone;
import eatyourbeets.relics.TheWolleyCore;

public class TheMaskedTraveler2 extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheMaskedTraveler2.class.getSimpleName());

    private final AbstractRelic relic1 = new TheEruzaStone();
    private final AbstractRelic relic2 = new TheWolleyCore();
    private final AbstractRelic relic3 = new TheEgnaroPiece();
    private final AbstractRelic medallion = new AncientMedallion(true);

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
        UpdateBodyText(eventStrings.DESCRIPTIONS[1], true);
        UpdateDialogOption(0, OPTIONS[2], relic1);
        UpdateDialogOption(1, OPTIONS[2], relic2);
        UpdateDialogOption(2, OPTIONS[2], relic3);
        UpdateDialogOption(3, OPTIONS[0]); // Leave
    }

    private void HandlePhase2(int button)
    {
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
        UpdateBodyText(eventStrings.DESCRIPTIONS[2], true);
        UpdateDialogOption(0, OPTIONS[2], medallion);
    }

    private void HandlePhase3(int button)
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[2], true);
        ObtainRelic(medallion);

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
}