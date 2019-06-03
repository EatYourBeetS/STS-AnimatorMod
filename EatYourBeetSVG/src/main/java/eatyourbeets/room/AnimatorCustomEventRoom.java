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
    private final AbstractEvent replacementEvent;
    private final EventRoom eventRoom = new EventRoom();

    public AnimatorCustomEventRoom(AbstractEvent replacementEvent)
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
        event = eventRoom.event = replacementEvent;
        eventRoom.event.onEnterRoom();
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll)
    {
        return eventRoom.getCardRarity(roll);
    }

    @Override
    public void update()
    {
        eventRoom.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        eventRoom.render(sb);
        eventRoom.renderEventTexts(sb);
    }

    @Override
    public void renderAboveTopPanel(SpriteBatch sb)
    {
        eventRoom.renderAboveTopPanel(sb);
    }
}
