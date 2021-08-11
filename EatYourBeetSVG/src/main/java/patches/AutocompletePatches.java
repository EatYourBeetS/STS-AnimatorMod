package patches;

import basemod.AutoComplete;
import basemod.DevConsole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;

import java.util.ArrayList;
import java.util.Stack;

public class AutocompletePatches
{
    private static final FieldInfo<Boolean> _noMatch = JUtils.GetField("noMatch", AutoComplete.class);
    private static final FieldInfo<ArrayList<String>> _suggestions = JUtils.GetField("suggestions", AutoComplete.class);
    private static final FieldInfo<Stack<AutoComplete.Pair>> _suggestionPairs = JUtils.GetField("suggestionPairs", AutoComplete.class);
    private static final MethodInfo.T0<String> _getTextWithoutRightmostSpaceToken = JUtils.GetMethod("getTextWithoutRightmostSpaceToken", AutoComplete.class);
    private static final MethodInfo.T2<Integer, String, String> _lastIndexOfRegex = JUtils.GetMethod("lastIndexOfRegex", AutoComplete.class, String.class, String.class);

    @SpirePatch(clz = AutoComplete.class, method = "fillInSuggestion")
    public static class AutocompletePatches_FillInSuggestion
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix()
        {
            final boolean noMatch = _noMatch.Get(null);
            final ArrayList<String> suggestions = _suggestions.Get(null);
            final Stack<AutoComplete.Pair> suggestionPairs = _suggestionPairs.Get(null);

            if (!noMatch && !suggestions.isEmpty() && !suggestionPairs.isEmpty())
            {
                final String textToInsert = suggestions.get(AutoComplete.selected + suggestionPairs.peek().start);
                final char separator1 = ':';
                final char separator2 = '_';

                if (textToInsert.lastIndexOf(separator1) > DevConsole.currentText.lastIndexOf(separator1))
                {
                    DevConsole.currentText = _getTextWithoutRightmostSpaceToken.Invoke(null) + textToInsert.substring(0, textToInsert.lastIndexOf(separator1)) + separator1;
                }
                else if (textToInsert.lastIndexOf(separator2) > DevConsole.currentText.lastIndexOf(separator2))
                {
                    DevConsole.currentText = _getTextWithoutRightmostSpaceToken.Invoke(null) + textToInsert.substring(0, textToInsert.lastIndexOf(separator2)) + separator2;
                }
                else
                {
                    DevConsole.currentText = _getTextWithoutRightmostSpaceToken.Invoke(null) + textToInsert + " ";
                    AutoComplete.reset();
                }

                AutoComplete.suggest(false);
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AutoComplete.class, method = "removeOneTokenUsingSpaceAndIdDelimiter")
    public static class AutocompletePatches_RemoveOneTokenUsingSpaceAndIdDelimiter
    {
        private static final ArrayList<Integer> delimiterIndexes = new ArrayList<>();

        @SpirePrefixPatch
        public static SpireReturn Prefix()
        {
            UpdateDelimiterIndexes(DevConsole.currentText);

            final int curTextLength = DevConsole.currentText.length();
            if (delimiterIndexes.size() > 0 && curTextLength > 0)
            {
                final char lastChar = DevConsole.currentText.charAt(curTextLength - 1);
                if (lastChar == ' ')
                {
                    DevConsole.currentText = DevConsole.currentText.substring(0, curTextLength - 1);
                }
                else if (delimiterIndexes.size() == 1 || (delimiterIndexes.get(0) < curTextLength - 1))
                {
                    DevConsole.currentText = DevConsole.currentText.substring(0, delimiterIndexes.get(0) + 1);
                }
                else
                {
                    DevConsole.currentText = DevConsole.currentText.substring(0, delimiterIndexes.get(1) + 1);
                }
            }
            else
            {
                DevConsole.currentText = "";
            }

            return SpireReturn.Return();
        }

        private static void UpdateDelimiterIndexes(String text)
        {
            delimiterIndexes.clear();

            for (int i = text.length() - 1; i >= 0; i--)
            {
                char c = text.charAt(i);
                if (c == ' ' || c == ':' || c == '_')
                {
                    delimiterIndexes.add(i);
                }
            }
        }
    }
}
