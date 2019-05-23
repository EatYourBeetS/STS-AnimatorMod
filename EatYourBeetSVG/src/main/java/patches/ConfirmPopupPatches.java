package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.screens.options.ConfirmPopup;
import eatyourbeets.powers.PlayerStatistics;

public class ConfirmPopupPatches
{
    @SpirePatch(clz = ConfirmPopup.class, method = "effect")
    public static class CardLibraryPatches_getCopy
    {
        @SpirePrefixPatch
        public static void Prefix(ConfirmPopup popup)
        {
            if (popup.type == ConfirmPopup.ConfirmType.ABANDON)
            {
                PlayerStatistics.OnAbandonRun();
            }
            else if (popup.type == ConfirmPopup.ConfirmType.EXIT)
            {
                PlayerStatistics.OnExitRun();
            }
        }
    }
}