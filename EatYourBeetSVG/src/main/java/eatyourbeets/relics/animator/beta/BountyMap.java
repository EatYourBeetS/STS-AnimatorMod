package eatyourbeets.relics.animator.beta;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public class BountyMap extends AnimatorRelic
{
    public static final String ID = CreateFullID(BountyMap.class);
    private static final WeightedList<String> possibleRooms = new WeightedList<>();
    private ArrayList<String> path;
    private int progress;

    static {
        possibleRooms.Add("?", 2);
        possibleRooms.Add("T", 1);
        possibleRooms.Add("M", 3);
        possibleRooms.Add("E", 2);
        possibleRooms.Add("$", 1);
        possibleRooms.Add("R", 1);
    }

    public BountyMap()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        path = new ArrayList<>();

        for (int i = 0; i < MathUtils.random(4,5); i++) {
            path.add(possibleRooms.Retrieve(rng, false));
        }
        progress = 0;
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (progress < path.size() - 1 && path.get(progress).equals(room.getMapSymbol())) {
            flash();
            progress += 1;
        }
        else if (progress >= path.size()) {
            //TODO add event here
            SetEnabled(false);
        }
    }
}