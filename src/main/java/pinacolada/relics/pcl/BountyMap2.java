package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.rooms.*;
import eatyourbeets.rooms.AnimatorCustomEventRoom;
import pinacolada.events.pcl.TheSecludedHarbor;

public class BountyMap2 extends AbstractBountyMap
{
    public static final String ID = CreateFullID(BountyMap2.class);

    public BountyMap2()
    {
        super(ID);
    }

    @Override
    public Class<? extends AbstractRoom> GetCurrentRequiredRoom() {
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

    @Override
    protected AnimatorCustomEventRoom.GetEvent GetEventConstructor() {
        return TheSecludedHarbor::new;
    }
}