package patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public class SingleRelicViewPopupPatches
{
    private static final FieldInfo<AbstractRelic> _relic = JUtils.GetField("relic", SingleRelicViewPopup.class);
    private static final FieldInfo<Float> _DESC_LINE_WIDTH = JUtils.GetField("DESC_LINE_WIDTH", SingleRelicViewPopup.class);
    private static final FieldInfo<Float> _DESC_LINE_SPACING = JUtils.GetField("DESC_LINE_SPACING", SingleRelicViewPopup.class);

    @SpirePatch(clz = SingleRelicViewPopup.class, method = "renderDescription", paramtypez = {SpriteBatch.class})
    public static class SingleRelicViewPopup_Open
    {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(SingleRelicViewPopup __instance, SpriteBatch sb)
        {
            AbstractRelic temp = _relic.Get(__instance);
            if (temp.isSeen && temp instanceof EYBRelic)
            {
                float width = _DESC_LINE_WIDTH.Get(null);
                float spacing = _DESC_LINE_SPACING.Get(null);
                float height = FontHelper.getSmartHeight(FontHelper.cardDescFont_N, temp.description, width, spacing) / 2.0F;
                RenderHelpers.WriteSmartText(sb, FontHelper.cardDescFont_N, temp.description, (float)Settings.WIDTH / 2.0F - 200.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F - 140.0F * Settings.scale - height, width, spacing, Settings.CREAM_COLOR);
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
