package eatyourbeets.room;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class AnimatorCustomEventRoom extends AbstractRoom
{
    public interface GetEvent
    {
        AbstractEvent get();
    }

    private final GetEvent replacementEvent;
    private EventRoom eventRoom;

    public AnimatorCustomEventRoom(GetEvent replacementEvent)
    {
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "??";
        this.mapImg = ImageMaster.MAP_NODE_EVENT;
        this.mapImgOutline = ImageMaster.MAP_NODE_EVENT_OUTLINE;
        this.replacementEvent = replacementEvent;
    }

    @Override
    public void onPlayerEntry()
    {
        AbstractDungeon.overlayMenu.proceedButton.hide();

        try
        {
            this.eventRoom = new EventRoom();
            this.event = eventRoom.event = replacementEvent.get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        eventRoom.event.onEnterRoom();
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll)
    {
        if (eventRoom != null)
        {
            return eventRoom.getCardRarity(roll);
        }
        else
        {
            return AbstractCard.CardRarity.COMMON;
        }
    }

    @Override
    public void update()
    {
        if (eventRoom != null)
        {
            eventRoom.update();
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (eventRoom != null)
        {
            eventRoom.render(sb);
            eventRoom.renderEventTexts(sb);
        }
    }

    @Override
    public void renderAboveTopPanel(SpriteBatch sb)
    {
        if (eventRoom != null)
        {
            eventRoom.renderAboveTopPanel(sb);
        }
    }
}
