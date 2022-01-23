package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.rooms.AnimatorCustomEventRoom;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.events.base.PCLEvent;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLJUtils;

public abstract class AbstractBountyMap extends PCLRelic
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
            this.usedUp();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);

        Class<? extends AbstractRoom> roomType = this.GetCurrentRequiredRoom();

        if (counter >= 0 && room != null && (room.getClass().equals(roomType) || "?".equals(room.getMapSymbol()))) {
            flash();
            if (roomType.equals(EventRoom.class)) {
                SetCounter(-1);
                this.usedUp();
                PCLEvent.ForceEvent(GetEventConstructor());
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
            return PCLJUtils.Format(DESCRIPTIONS[0], DESCRIPTIONS[1] + name);
        } else {
            return PCLJUtils.Format(DESCRIPTIONS[0], "");
        }
    }

    private void fixDescription()
    {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PCLCardTooltip(this.name, this.description));
        this.initializeTips();
    }

    protected Class<? extends AbstractRoom> GetCurrentRequiredRoom() {
        return EventRoom.class;
    }

    protected abstract AnimatorCustomEventRoom.GetEvent GetEventConstructor();
}