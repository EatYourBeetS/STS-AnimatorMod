package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.VividPicture;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.regex.Pattern;

public class RelicLibraryPatches
{
    @SpirePatch(clz = RelicLibrary.class, method = "getRelic", paramtypez = {String.class})
    public static class RelicLibraryPatches_GetRelic
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractRelic> Prefix(String key)
        {
            String baseID;
            EnchantableRelic relic;
            if (key.startsWith(LivingPicture.ID))
            {
                baseID = LivingPicture.ID + ":";
                relic = new LivingPicture();
            }
            else if (key.startsWith(VividPicture.ID))
            {
                baseID = VividPicture.ID + ":";
                relic = new VividPicture();
            }
            else
            {
                return SpireReturn.Continue();
            }

            if (GR.IsLoaded && key.length() > baseID.length())
            {
                final String[] text = key.substring(baseID.length()).split(Pattern.quote(":"));
                final Enchantment e = Enchantment.GetCard(JUtils.ParseInt(text[0], 1), JUtils.ParseInt(text[1], 0));
                relic.ApplyEnchantment(e);
            }

            return SpireReturn.Return(relic);
        }
    }
}