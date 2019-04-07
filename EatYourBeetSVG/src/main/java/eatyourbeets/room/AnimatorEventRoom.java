package eatyourbeets.room;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class AnimatorEventRoom extends EventRoom
{
    public AnimatorEventRoom(AbstractEvent event)
    {
        this.event = event;
    }

    public void onPlayerEntry()
    {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.event.onEnterRoom();
    }
}

