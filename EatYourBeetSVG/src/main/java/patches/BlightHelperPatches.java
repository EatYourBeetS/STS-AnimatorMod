package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import eatyourbeets.blights.Doomed;
import eatyourbeets.blights.Haunted;

@SpirePatch(clz= BlightHelper.class, method = "getBlight", paramtypez = {String.class})
public class BlightHelperPatches
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractBlight> Method(String id)
    {
        if (Haunted.ID.equals(id))
        {
            return SpireReturn.Return(new Haunted());
        }
        else if (Doomed.ID.equals(id))
        {
            return SpireReturn.Return(new Doomed());
        }
        else
        {
            return SpireReturn.Continue();
        }
    }
}