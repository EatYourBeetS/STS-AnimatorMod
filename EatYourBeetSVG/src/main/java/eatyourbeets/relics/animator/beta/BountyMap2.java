package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.*;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.events.animator.TheMysteriousPeak;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.JUtils;

public class BountyMap2 extends AnimatorRelic
{
    public static final String ID = CreateFullID(BountyMap2.class);

    public BountyMap2()
    {
        super(ID, BountyMap.ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        SetCounter(0);

        fixDescription();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);

        Class<? extends AbstractRoom> roomType = this.GetCurrentRequiredRoom();

        if (room.getClass().equals(roomType) && this.IsEnabled()) {
            flash();
            if (roomType.equals(EventRoom.class)) {
                SetEnabled(false);
                EYBEvent newRoom = new TheMysteriousPeak();
                newRoom.onEnterRoom();
            }
            else {
                AddCounter(1);
                fixDescription();
            }
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        Class<? extends AbstractRoom> room = this.GetCurrentRequiredRoom();
        if (CardCrawlGame.isInARun() && !room.equals(EventRoom.class)) {
            String name = room.equals(MonsterRoomElite.class) ? "Elite" : room.getSimpleName().split("Room")[0];
            return JUtils.Format(DESCRIPTIONS[0], "NL Current Required Room: #b" + name);
        } else {
            return JUtils.Format(DESCRIPTIONS[0], "");
        }
    }

    private Class<? extends AbstractRoom> GetCurrentRequiredRoom() {
        switch (counter) {
            case 0:
                return MonsterRoom.class;
            case 1:
                return ShopRoom.class;
            case 2:
                return RestRoom.class;
            case 3:
                return MonsterRoomElite.class;
            default:
                return EventRoom.class;
        }
    }

    private void fixDescription()
    {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new EYBCardTooltip(this.name, this.description));
        this.initializeTips();
    }
}