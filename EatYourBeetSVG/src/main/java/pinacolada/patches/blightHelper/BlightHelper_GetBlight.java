package pinacolada.patches.blightHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import pinacolada.blights.common.UpgradedHand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@SpirePatch(clz= BlightHelper.class, method = "getBlight", paramtypez = {String.class})
public class BlightHelper_GetBlight
{
    private static final HashMap<String, Class<? extends AbstractBlight>> customBlights = new HashMap<>();

    @SpirePrefixPatch
    public static SpireReturn<AbstractBlight> Method(String id)
    {
        final Class<? extends AbstractBlight> blight = customBlights.get(id);
        if (blight != null)
        {
            try
            {
                return SpireReturn.Return(blight.getConstructor().newInstance());
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return SpireReturn.Continue();
    }

    static
    {
        customBlights.put(UpgradedHand.ID, UpgradedHand.class);
    }
}