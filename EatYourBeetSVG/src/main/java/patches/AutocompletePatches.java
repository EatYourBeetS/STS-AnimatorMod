package patches;

import basemod.AutoComplete;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

public class AutocompletePatches
{
    @SpirePatch(clz = AutoComplete.class, method = "fillInSuggestion")
    public static class AutocompletePatches_FillInSuggestion
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix()
        {
            //TODO: Autocomplete with "_" in addition to ":"
            return SpireReturn.Continue();
        }
    }
}
