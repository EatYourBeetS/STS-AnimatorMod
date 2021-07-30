package eatyourbeets.relics.animator.beta;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.events.animator.TheMysteriousPeak;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public class BountyMap extends AnimatorRelic
{
    public static final String ID = CreateFullID(BountyMap.class);
    private static final WeightedList<String> possibleRooms = new WeightedList<>();
    private ArrayList<String> path;
    private int progress;

    public BountyMap()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (possibleRooms.Size() == 0) {
            possibleRooms.Add("?", 2);
            possibleRooms.Add("T", 1);
            possibleRooms.Add("M", 3);
            possibleRooms.Add("E", 2);
            possibleRooms.Add("$", 1);
            possibleRooms.Add("R", 1);
        }

        path = new ArrayList<>();
        for (int i = 0; i < MathUtils.random(3,5); i++) {
            path.add(possibleRooms.Retrieve(rng, false));
        }
        path.add("?");
        progress = 0;

        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new EYBCardTooltip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (progress < path.size() - 1 && path.get(progress).equals(room.getMapSymbol())) {
            flash();
            progress += 1;
        }
        if (progress >= path.size() && this.IsEnabled()) {
            SetEnabled(false);
            EYBEvent newRoom = new TheMysteriousPeak();
            newRoom.onEnterRoom();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        if (CardCrawlGame.isInARun()) {
            return JUtils.Format(DESCRIPTIONS[0], String.join(" ", path));
        } else {
            return JUtils.Format(DESCRIPTIONS[0], "...");
        }
    }
}