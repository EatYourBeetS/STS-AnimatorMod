package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import eatyourbeets.effects.special.GenericChooseCardsToRemoveEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameEffects;

public class StarCompass extends AnimatorRelic
{
    public static final String ID = CreateFullID(StarCompass.class);

    private boolean awaitingInput;

    public StarCompass()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);
        if (room instanceof EventRoom || room instanceof TreasureRoom || room instanceof RestRoom) {
            flash();
            GameEffects.TopLevelList.Add(new GenericChooseCardsToRemoveEffect(1));
        }

    }
}