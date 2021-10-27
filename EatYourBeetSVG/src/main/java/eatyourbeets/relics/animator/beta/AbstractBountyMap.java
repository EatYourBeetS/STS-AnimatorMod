package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.rooms.AnimatorCustomEventRoom;
import eatyourbeets.utilities.JUtils;

public abstract class AbstractBountyMap extends AnimatorRelic
{
    public AbstractBountyMap(String id)
    {
        super(id, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        SetCounter(0);

        fixDescription();
    }

    @Override
    public void update() {
        super.update();
        if (counter < 0) {
            SetEnabled(false);
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);

        Class<? extends AbstractRoom> roomType = this.GetCurrentRequiredRoom();

        if (room.getClass().equals(roomType) && this.IsEnabled()) {
            flash();
            if (roomType.equals(EventRoom.class)) {
                SetCounter(-1);
                SetEnabled(false);
                EYBEvent.ForceEvent(GetEventConstructor());
            }
            else {
                AddCounter(1);
            }
            fixDescription();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        Class<? extends AbstractRoom> room = this.GetCurrentRequiredRoom();
        if (CardCrawlGame.isInARun() && counter >= 0) {
            String name = room.equals(MonsterRoomElite.class) ? "Elite" : room.getSimpleName().split("Room")[0];
            return JUtils.Format(DESCRIPTIONS[0], DESCRIPTIONS[1] + name);
        } else {
            return JUtils.Format(DESCRIPTIONS[0], "");
        }
    }

    private void fixDescription()
    {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new EYBCardTooltip(this.name, this.description));
        this.initializeTips();
    }

    protected Class<? extends AbstractRoom> GetCurrentRequiredRoom() {
        return EventRoom.class;
    }

    protected abstract AnimatorCustomEventRoom.GetEvent GetEventConstructor();
}