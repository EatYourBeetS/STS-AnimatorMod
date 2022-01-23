package pinacolada.events.base;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.Ghosts;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.rooms.AnimatorCustomEventRoom;
import pinacolada.events.pcl.*;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;

import java.util.ArrayList;

public abstract class PCLEvent extends EYBEvent
{
    public static final PCLImages.Events IMAGES = GR.PCL.Images.Events;
    public final ArrayList<EYBEventPhase> phases = new ArrayList<>();
    public EYBEventPhase currentPhase;
    public final String id;

    public static String CreateFullID(Class<? extends EYBEvent> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public static AbstractEvent GenerateSpecialEvent(AbstractDungeon dungeon, Random rng, boolean isAnimator)
    {
        if (isAnimator)
        {
            PCLEvent event = TheCursedForest.TryCreate(rng);
            if (event == null) {
                event = ThePharmacy.TryCreate(rng);
            }
            if (event == null) {
                event = TheFloatyThing.TryCreate(rng);
            }
            if (event == null) {
                event = MicrowaveEvent.TryCreate(rng);
            }
            if (event == null) {
                event = TheHeroAssociation.TryCreate(rng);
            }
            if (event == null) {
                event = ContractEvent.TryCreate(rng);
            }

            if (event != null) {
                GR.PCL.Dungeon.SetMapData(event.id, "");
            }
            return event;
        }

        return null;
    }

    public static void ForceEvent(AnimatorCustomEventRoom.GetEvent roomConstructor) {
        MapRoomNode node = new MapRoomNode(AbstractDungeon.currMapNode.x, AbstractDungeon.currMapNode.y);
        node.room = new AnimatorCustomEventRoom(roomConstructor);
        for (MapEdge edge : AbstractDungeon.currMapNode.getEdges()) {
            node.addEdge(edge);
        }

        RoomEventDialog.optionList.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.previousScreen = null;
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.fadeIn();
        AbstractDungeon.effectList.clear();
        AbstractDungeon.topLevelEffects.clear();
        AbstractDungeon.topLevelEffectsQueue.clear();
        AbstractDungeon.effectsQueue.clear();
        AbstractDungeon.dungeonMapScreen.dismissable = true;
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.setCurrMapNode(node);
        AbstractDungeon.getCurrRoom().onPlayerEntry();
        AbstractDungeon.scene.nextRoom(node.room);
        AbstractDungeon.rs = node.room.event instanceof AbstractImageEvent ? AbstractDungeon.RenderScene.EVENT : AbstractDungeon.RenderScene.NORMAL;
    }

    public static void UpdateEvents(boolean isPCL)
    {
        if (isPCL)
        {
            AbstractDungeon.eventList.remove(Vampires.ID);
            AbstractDungeon.eventList.remove(Ghosts.ID);
        }
    }

    public static void RegisterEvents()
    {
        BaseMod.addEvent(ContractEvent.ID, ContractEvent.class, ContractEvent.ID);
        BaseMod.addEvent(MicrowaveEvent.ID, MicrowaveEvent.class, MicrowaveEvent.ID);
        BaseMod.addEvent(TheCursedForest.ID, TheCursedForest.class, TheCursedForest.ID);
        BaseMod.addEvent(TheFloatyThing.ID, TheFloatyThing.class, TheFloatyThing.ID);
        BaseMod.addEvent(TheHeroAssociation.ID, TheHeroAssociation.class, TheHeroAssociation.ID);
        BaseMod.addEvent(ThePharmacy.ID, ThePharmacy.class, ThePharmacy.ID);
    }

    public PCLEvent(String id, EYBEventStrings strings)
    {
        this(id, strings, GR.PCL.Images.Events.Placeholder.Path());
    }

    public PCLEvent(String id, EYBEventStrings strings, String imageUrl)
    {
        super(id, strings, imageUrl);
        this.id = id;
    }
}
