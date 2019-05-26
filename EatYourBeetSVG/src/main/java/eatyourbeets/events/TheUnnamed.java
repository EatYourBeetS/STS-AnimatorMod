package eatyourbeets.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.dungeons.TheUnnamedReign;

public class TheUnnamed extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheUnnamed.class.getSimpleName());

    private int phase;

    private final int CHOICE_CONTINUE = 0;
    private final int CHOICE_ACCEPT = 0;
    private final int CHOICE_SIGN = 0;
    private final int CHOICE_LEAVE = 1;
    private final int CHOICE_TOOSTRONG   = 0;
    private final int CHOICE_VERYSTRONG  = 1;
    private final int CHOICE_STRONG      = 2;
    private final int CHOICE_WEAK        = 3;
    private final int CHOICE_VERYWEAK    = 4;

    public TheUnnamed()
    {
        super(ID);

        this.phase = 0;
        this.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);
        this.imageEventText.updateDialogOption(CHOICE_CONTINUE, OPTIONS[1]);
        this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        boolean leave = true;

        if (phase == 0)
        {
            leave = Inquire(buttonPressed);
        }
        else
        {
            this.imageEventText.updateDialogOption(CHOICE_LEAVE, OPTIONS[0]);
        }

        if (leave)
        {
            this.openMap();
        }
    }

    private boolean Inquire(int button)
    {
        if (button == CHOICE_CONTINUE)
        {
            AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

            CardCrawlGame.nextDungeon = TheUnnamedReign.ID;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

            GenericEventDialog.hide();
            AbstractDungeon.fadeOut();
            AbstractDungeon.isDungeonBeaten = true;

            phase = 1;

            return false;
        }

        return true;
    }
}