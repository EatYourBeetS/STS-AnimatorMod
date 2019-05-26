package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.events.TheDomVedeloper1;
import eatyourbeets.events.TheMaskedTraveler1;
import eatyourbeets.relics.*;

public class AbstractDungeonPatches
{
    @SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
    public static class AbstractDungeonPatches_InitializeCardPools
    {
        @SpirePrefixPatch
        public static void Prefix(AbstractDungeon dungeon_instance)
        {
            if (!(AbstractDungeon.player instanceof AnimatorCharacter))
            {
                AbstractDungeon.eventList.remove(TheMaskedTraveler1.ID);
                AbstractDungeon.eventList.remove(TheDomVedeloper1.ID);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "returnEndRandomRelicKey")
    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomRelicKey")
    public static class AbstractDungeonPatches_ReturnRandomRelicKey
    {
        @SpirePrefixPatch
        public static SpireReturn<String> Prefix(AbstractRelic.RelicTier tier)
        {
            for (AbstractRelic relic : AbstractDungeon.player.relics)
            {
                if (relic instanceof UnnamedReignRelic)
                {
                    return SpireReturn.Return(TheAncientMedallion.ID);
                }
            }

            return SpireReturn.Continue();
        }
    }
}