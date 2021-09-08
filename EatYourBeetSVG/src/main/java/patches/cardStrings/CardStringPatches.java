package patches.cardStrings;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.localization.CardStrings;

@SpirePatch(
        clz = CardStrings.class,
        method = "<class>"
)
public class CardStringPatches {
    public static SpireField<String[]> ALTERNATE_DESCRIPTION = new SpireField(() -> {
        return new String[]{};
    });

    public CardStringPatches() {
    }
}