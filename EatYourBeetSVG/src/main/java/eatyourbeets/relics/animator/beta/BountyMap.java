package eatyourbeets.relics.animator.beta;

import basemod.abstracts.CustomSavable;
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

public class BountyMap extends AnimatorRelic implements CustomSavable<ArrayList<String>>
{
    public static final String ID = CreateFullID(BountyMap.class);
    private static final WeightedList<String> possibleRooms = new WeightedList<>();
    private ArrayList<String> path = new ArrayList<>();

    public BountyMap()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (possibleRooms.Size() == 0) {
            possibleRooms.Add("?", 3);
            possibleRooms.Add("T", 1);
            possibleRooms.Add("M", 4);
            possibleRooms.Add("E", 2);
            possibleRooms.Add("$", 1);
            possibleRooms.Add("R", 1);
        }

        path = new ArrayList<>();
        for (int i = 0; i < MathUtils.random(3,5); i++) {
            path.add(possibleRooms.Retrieve(rng, false));
        }
        path.add("?");
        SetCounter(0);

        fixDescription();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);

        if (counter < path.size() - 1 && path.get(counter).equals(room.getMapSymbol())) {
            flash();
            AddCounter(1);
            fixDescription();
        }
        if (counter >= path.size() && this.IsEnabled()) {
            flash();
            SetEnabled(false);
            EYBEvent newRoom = new TheMysteriousPeak();
            newRoom.onEnterRoom();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        if (CardCrawlGame.isInARun() && path != null && path.size() > 0) {
            return JUtils.Format(DESCRIPTIONS[0], String.join(" #y", path), counter);
        } else {
            return JUtils.Format(DESCRIPTIONS[0], "...", counter);
        }
    }

    @Override
    public ArrayList<String> onSave()
    {
        return this.path;
    }

    @Override
    public void onLoad(ArrayList<String> strings)
    {
        if (strings != null) {
            this.path.addAll(strings);
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