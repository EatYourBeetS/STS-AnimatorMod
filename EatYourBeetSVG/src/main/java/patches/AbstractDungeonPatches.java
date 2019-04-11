package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.events.TheMaskedTraveler1;

@SpirePatch(clz= AbstractDungeon.class,method="initializeCardPools")
public class AbstractDungeonPatches
{
    @SpirePrefixPatch
    public static void Prefix(AbstractDungeon dungeon_instance)
    {
        if (!(AbstractDungeon.player instanceof AnimatorCharacter))
//                || Settings.language == Settings.GameLanguage.ZHT  //
//                || Settings.language == Settings.GameLanguage.ZHS) // waiting for translation
        {
            AbstractDungeon.eventList.remove(TheMaskedTraveler1.ID);
        }
    }
}
