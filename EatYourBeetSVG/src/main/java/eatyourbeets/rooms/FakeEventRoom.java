package eatyourbeets.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class FakeEventRoom extends AbstractRoom
{
    public static MapRoomNode MapRoomNode;
    public static FakeEventRoom Instance;
    static
    {
        MapRoomNode = new MapRoomNode(0, 0);
        MapRoomNode.room = Instance = new FakeEventRoom();
    }

    private FakeEventRoom()
    {
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "??";
    }

    @Override
    public void onPlayerEntry()
    {

    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll)
    {
        return AbstractCard.CardRarity.COMMON;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void render(SpriteBatch sb)
    {

    }

    @Override
    public void renderAboveTopPanel(SpriteBatch sb)
    {

    }
}
