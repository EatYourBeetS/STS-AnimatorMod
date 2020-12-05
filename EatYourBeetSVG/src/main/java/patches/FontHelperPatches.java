package patches;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import javassist.CtBehavior;

public class FontHelperPatches
{
    // This patch ensures each font has a handle to its own file...
    @SpirePatch(clz = FontHelper.class, method = "prepFont", paramtypez = {FreeTypeFontGenerator.class, float.class, boolean.class})
    public static class FontHelperPatches_prepFont
    {
        private static final FieldInfo<FileHandle> _fontFile = JUtils.GetField("fontFile", FontHelper.class);

        @SpireInsertPatch(localvars = {"font"}, locator = Locator.class)
        public static void Method(FreeTypeFontGenerator g, float size, boolean isLinearFiltering, BitmapFont font)
        {
            font.getData().fontFile = _fontFile.Get(null);
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(BitmapFont.class, "setUseIntegerPositions");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

//    @SpirePatch(clz = FontHelper.class, method = "ClearSCPFontTextures")
//    @SpirePatch(clz = FontHelper.class, method = "ClearSRVFontTextures")
//    @SpirePatch(clz = FontHelper.class, method = "ClearLeaderboardFontTextures")
//    public static class FontHelperPatches_ClearSCPFontTextures
//    {
//        @SpirePrefixPatch
//        public static SpireReturn Method()
//        {
//            return SpireReturn.Return(null);
//        }
//    }
}