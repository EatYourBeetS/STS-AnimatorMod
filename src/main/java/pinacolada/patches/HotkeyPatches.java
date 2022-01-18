package pinacolada.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.RemapInputElement;
import javassist.CtBehavior;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;

import java.util.ArrayList;

@SpirePatch(
        clz=InputSettingsScreen.class,
        method="refreshData"
)
public class HotkeyPatches
{

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"elements"}
    )
    public static void Insert(InputSettingsScreen __instance, ArrayList<RemapInputElement> elements)
    {
        if (!Settings.isControllerMode) {
            elements.add(new RemapInputElement(__instance, GR.PCL.Strings.Hotkeys.ControlPileChange, PCLHotkeys.controlPileChange));
            elements.add(new RemapInputElement(__instance, GR.PCL.Strings.Hotkeys.ControlPileSelect, PCLHotkeys.controlPileSelect));
            elements.add(new RemapInputElement(__instance, GR.PCL.Strings.Hotkeys.Cycle, PCLHotkeys.cycle));
            elements.add(new RemapInputElement(__instance, GR.PCL.Strings.Hotkeys.RerollCurrent, PCLHotkeys.rerollCurrent));
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputSettingsScreen.class, "maxScrollAmount");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    @SpirePatch(
            clz= InputActionSet.class,
            method="load"
    )
    public static class Load
    {
        public static void Prefix()
        {
            PCLHotkeys.load();
        }
    }

    @SpirePatch(
            clz=InputActionSet.class,
            method="save"
    )
    public static class Save
    {
        public static void Prefix()
        {
            PCLHotkeys.save();
        }
    }

    @SpirePatch(
            clz=InputActionSet.class,
            method="resetToDefaults"
    )
    public static class Reset
    {
        public static void Prefix()
        {
            PCLHotkeys.resetToDefaults();
        }
    }
}