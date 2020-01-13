package patches.abstractDungeon;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Ghosts;
import eatyourbeets.events.animator.TheDomVedeloper1;
import eatyourbeets.events.animator.TheMaskedTraveler1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
public class AbstractDungeon_InitializeCardPools
{
    @SpirePrefixPatch
    public static void Prefix(AbstractDungeon dungeon_instance)
    {
        if (AbstractDungeon.player.chosenClass == GR.Enums.Characters.THE_ANIMATOR)
        {
            if (GameUtilities.GetActualAscensionLevel() >= 17)
            {
                AbstractDungeon.eventList.remove(Ghosts.ID);
            }
        }
        else
        {
            AbstractDungeon.eventList.remove(TheMaskedTraveler1.ID);
            AbstractDungeon.eventList.remove(TheDomVedeloper1.ID);
        }
    }
}