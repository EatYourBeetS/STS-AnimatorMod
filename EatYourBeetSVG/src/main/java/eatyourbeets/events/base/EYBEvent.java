package eatyourbeets.events.base;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.TheJoust;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.events.animator.*;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class EYBEvent extends AbstractImageEvent
{
    public static final CommonImages.Events IMAGES = GR.Common.Images.Events;
    public static final EYBCommonStrings COMMON_STRINGS = new EYBCommonStrings();
    public final ArrayList<EYBEventPhase> phases = new ArrayList<>();
    public final EYBEventStrings strings;
    public EYBEventPhase currentPhase;

    // TODO: move these to an AnimatorEvent class
    public static String CreateFullID(Class<? extends EYBEvent> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public static AbstractEvent GenerateSpecialEvent(AbstractDungeon dungeon, Random rng, boolean isAnimator)
    {
        if (isAnimator)
        {
            return TheCursedForest.TryCreate(rng);
        }

        return null;
    }

    public static void UpdateEvents(boolean isAnimator, boolean isAnimatorClassic)
    {
        if (!isAnimator)
        {
            AbstractDungeon.eventList.remove(TheMaskedTraveler1.ID);
        }
        else
        {
            AbstractDungeon.eventList.remove(TheJoust.ID);
        }
    }

    public static void RegisterEvents()
    {
        BaseMod.addEvent(TheMaskedTraveler1.ID, TheMaskedTraveler1.class, Exordium.ID);
        //BaseMod.addEvent(TheMaskedTraveler2.ID, TheMaskedTraveler2.class, TheEnding.ID);
        BaseMod.addEvent(TheMaskedTraveler3.ID, TheMaskedTraveler3.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheHaunt.ID, TheHaunt.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheUnnamedMerchant.ID, TheUnnamedMerchant.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheAbandonedCabin.ID, TheAbandonedCabin.class, TheUnnamedReign.ID);
    }

    public EYBEvent(String id, EYBEventStrings strings)
    {
        this(id, strings, GR.Common.Images.Events.Placeholder.Path());
    }

    public EYBEvent(String id, EYBEventStrings strings, String imageUrl)
    {
        super("NAME", "BODY", imageUrl);

        this.strings = strings.SetStrings(GR.GetEventStrings(id));
        this.title = strings.name;
        this.body = strings.GetDescription(0);
        this.currentPhase = null;
    }

    @Override
    protected final void buttonEffect(int i)
    {
        if (currentPhase == null || !currentPhase.OnOptionSelected(i))
        {
            OpenMap();
        }
    }

    protected void RegisterSpecialPhase(EYBEventPhase phase)
    {
        RegisterPhase(-1, phase);
    }

    protected void RegisterPhase(int index, EYBEventPhase phase)
    {
        phases.add(phase);

        phase.index = index;
        phase.dialog = this.imageEventText;
        phase.text = this.strings;
        phase.event = this;
    }

    public void ChangePhase(Class<? extends EYBEventPhase> newPhase)
    {
        for (EYBEventPhase phase : phases)
        {
            if (newPhase.equals(phase.getClass()))
            {
                currentPhase = phase;

                phase.OnEnter();
                phase.BuildOptions();
                return;
            }
        }

        JUtils.LogInfo(this, "Event phase not found: " + newPhase.getSimpleName());
        OpenMap();
    }

    public void ChangePhase(int newPhase)
    {
        for (EYBEventPhase phase : phases)
        {
            if (newPhase == phase.index)
            {
                currentPhase = phase;

                phase.OnEnter();
                phase.BuildOptions();
                return;
            }
        }

        JUtils.LogInfo(this, "Event phase not found: " + newPhase);
        OpenMap();
    }

    public void ProgressPhase()
    {
        if (currentPhase == null)
        {
            ChangePhase(0);
        }
        else if (currentPhase.index == -1)
        {
            OpenMap();
        }
        else
        {
            ChangePhase(currentPhase.index + 1);
        }
    }

    public void OpenMap()
    {
        super.openMap();
    }

    public static class EYBCommonStrings extends EYBEventStrings
    {
        private EYBCommonStrings Load()
        {
            if (name == null)
            {
                SetStrings(GR.GetEventStrings("_CommonEvent"));
            }

            return this;
        }

        public final String Continue()
        {
            return Load().GetOption(0);
        }

        public final String Leave()
        {
            return Load().GetOption(1);
        }
    }
}
