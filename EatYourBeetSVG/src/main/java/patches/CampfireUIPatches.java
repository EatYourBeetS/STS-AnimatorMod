package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.EnchantCampfireOption;
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
            GR.UI.CampfireUI.Reset();

            if (EnchantCampfireOption.CanAddOption())
            {
                ArrayList<AbstractCampfireOption> buttons = _buttons.Get(__instance);
                buttons.add(new EnchantCampfireOption());

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
    }

    @SpirePatch(clz = CampfireUI.class, method = "update")
    public static class CampfireUIPatches_Update
    {
        @SpirePostfixPatch
        public static void Postfix(CampfireUI __instance)
        {
            GR.UI.CampfireUI.Update();
        }
    }

    @SpirePatch(clz = CampfireUI.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class CampfireUIPatches_Render
    {
        @SpirePostfixPatch
        public static void Postfix(CampfireUI __instance, SpriteBatch sb)
        {
            GR.UI.CampfireUI.Render(sb);
        }
    }
}
