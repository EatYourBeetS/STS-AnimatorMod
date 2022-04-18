package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameEffects;

public class ShinigamiFerry extends AnimatorRelic implements OnRelicObtainedSubscriber
{
    public static final String ID = CreateFullID(ShinigamiFerry.class);

    public ShinigamiFerry()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);

        SetCounter(1);
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        super.onEnterRoom(room);

        if (counter > 0)
        {
            AddCounter(-1);
            flash();
        }
    }

    @Override
    public void OnRelicObtained(AbstractRelic relic, Trigger trigger)
    {
        if (trigger == Trigger.Equip && relic != this && relic instanceof ShinigamiFerry)
        {
            relic.currentX = relic.targetX = Settings.WIDTH * 100;
            GameEffects.Queue.RemoveRelic(relic).AddCallback(r -> AddCounter(r.counter));
        }
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        if (!SetEnabled(this.counter > 0))
        {
            this.counter = 0;
        }
    }
}