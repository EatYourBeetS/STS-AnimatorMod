package patches;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import javassist.CtBehavior;

// This patch ensures each font has a handle to its own file...
@SpirePatch(clz = FontHelper.class, method = "prepFont", paramtypez = {FreeTypeFontGenerator.class, float.class, boolean.class})
public class FontHelperPatches
{
    private static final FieldInfo<FileHandle> _fontFile = JavaUtilities.GetField("fontFile", FontHelper.class);

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
