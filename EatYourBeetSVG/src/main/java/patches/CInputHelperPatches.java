package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import eatyourbeets.utilities.InputManager;

public class CInputHelperPatches
{
    @SpirePatch(clz= CInputHelper.class, method="listenerPress")
    public static class CInputHelperPatches_listenerPress
    {
        @SpirePrefixPatch
        public static void Method(int keycode)
        {
            InputManager.OnControllerKeyPress(keycode);
        }
    }

    @SpirePatch(clz= CInputHelper.class, method="listenerRelease")
    public static class CInputHelperPatches_listenerRelease
    {
        @SpirePrefixPatch
        public static void Method(int keycode)
        {
            InputManager.OnControllerKeyRelease(keycode);
        }
    }
}
