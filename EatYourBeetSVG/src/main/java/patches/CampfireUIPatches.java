package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class CampfireUIPatches
{
    protected static final FieldInfo<ArrayList<AbstractCampfireOption>> _buttons = JUtils.GetField("buttons", CampfireUI.class);

    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class CampfireUIPatches_initializeButtons
    {
        @SpirePostfixPatch
        public static void Postfix(CampfireUI __instance)
        {
            ArrayList<AbstractCampfireOption> buttons = _buttons.Get(__instance);
            //if (EnchantCampfireOption.CanAddOption())
            //{
            //    AddOption(__instance, new EnchantCampfireOption());
            //}
            //if (KirbyCampfireOption.CanAddOption())
            //{
            //    AddOption(__instance, new KirbyCampfireOption());
            //}
        }
    }

    protected static void AddOption(CampfireUI instance, AbstractCampfireOption option) {
        ArrayList<AbstractCampfireOption> buttons = _buttons.Get(instance);
        buttons.add(option);

        float x = 950.f;
        float y = 990.0f - (270.0f * ((buttons.size() + 1) * 0.5f));
        if (buttons.size() % 2 == 0)
        {
            x = 1110.0f;
            buttons.get(buttons.size() - 2)
                    .setPosition(800.0f * Settings.scale, y * Settings.scale);
        }

        buttons.get(buttons.size() - 1)
                .setPosition(x * Settings.scale, y * Settings.scale);
    }
}
