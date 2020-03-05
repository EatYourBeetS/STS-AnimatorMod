package eatyourbeets.events.base;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.events.UnnamedReign.TheAbandonedCabin;
import eatyourbeets.events.UnnamedReign.TheHaunt;
import eatyourbeets.events.UnnamedReign.TheMaskedTraveler3;
import eatyourbeets.events.UnnamedReign.TheUnnamedMerchant;
import eatyourbeets.events.animator.TheMaskedTraveler1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class EYBEvent extends AbstractImageEvent
{
    protected final Map<Integer, EventPhase> phases = new HashMap<>();
    protected final ArrayList<PhaseHandler> phaseHandlers = new ArrayList<>();

    public EYBEventStrings strings;
    public int currentPhase;

    public static void RegisterEvents()
    {
        BaseMod.addEvent(TheMaskedTraveler1.ID, TheMaskedTraveler1.class, Exordium.ID);
        //BaseMod.addEvent(TheMaskedTraveler1_New.ID, TheMaskedTraveler1_New.class, Exordium.ID);
        //BaseMod.addEvent(TheMaskedTraveler2.ID, TheMaskedTraveler2.class, TheEnding.ID);
        BaseMod.addEvent(TheMaskedTraveler3.ID, TheMaskedTraveler3.class, TheUnnamedReign.ID);
        //BaseMod.addEvent(TheDomVedeloper1.ID, TheDomVedeloper1.class, Exordium.ID);
        BaseMod.addEvent(TheHaunt.ID, TheHaunt.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheUnnamedMerchant.ID, TheUnnamedMerchant.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheAbandonedCabin.ID, TheAbandonedCabin.class, TheUnnamedReign.ID);
    }

    public EYBEvent(String id, EYBEventStrings strings)
    {
        this(id, strings, "placeholder.jpg");
    }

    public EYBEvent(String id, EYBEventStrings strings, String imageUrl)
    {
        super("NAME", "BODY", "images/events/" + imageUrl);

        this.strings = strings.SetStrings(GR.GetEventStrings(id));
        this.title = strings.name;
        this.body = strings.GetDescription(0);
        this.currentPhase = 0;
    }

    @Override
    protected final void buttonEffect(int i)
    {
        EventPhase phase = phases.get(currentPhase);
        if (phase == null || !phase.OnOptionSelected(i))
        {
            OpenMap();
        }
    }

    protected void RegisterPhase(int id, EventPhase phase)
    {
        phases.put(id, phase);

        phase.id = id;
        phase.player = AbstractDungeon.player;
        phase.dialog = this.imageEventText;
        phase.text = this.strings;
        phase.event = this;
    }

    public void ChangePhase(int newPhase)
    {
        currentPhase = newPhase;

        EventPhase phase = phases.get(currentPhase);
        if (phase != null)
        {
            phase.OnEnter();
            phase.BuildOptions();
            return;
        }

        JavaUtilities.Log(this, "Event phase not found: " + currentPhase);
        OpenMap();
    }

    public void ProgressPhase()
    {
        ChangePhase(currentPhase + 1);
    }

    public void OpenMap()
    {
        super.openMap();
    }
}
