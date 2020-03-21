package patches.abstractStance;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.stances.EYBStance;

@SpirePatch(clz = AbstractStance.class, method = "getStanceFromName", paramtypez = {String.class})
public class AbstractStancePatches_getStanceFromName
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractStance> Prefix(String name)
    {
        AbstractStance stance = EYBStance.GetStanceFromName(name);
        if (stance != null)
        {
            return SpireReturn.Return(stance);
        }

        return SpireReturn.Continue();
    }
}
