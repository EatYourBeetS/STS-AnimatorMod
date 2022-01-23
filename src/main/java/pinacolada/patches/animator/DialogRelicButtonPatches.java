package pinacolada.patches.animator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.events.base.DialogRelicButton;
import eatyourbeets.utilities.FieldInfo;
import javassist.CtBehavior;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLJUtils;

public class DialogRelicButtonPatches
{
    private final static FieldInfo<AbstractRelic> _relicPreview = PCLJUtils.GetField("relicPreview", DialogRelicButton.class);

    @SpirePatch(
            clz= DialogRelicButton.class,
            method="render"
    )
    public static class DialogRelicButton_Render
    {
        @SpireInsertPatch(
                locator= Locator.class
        )
        public static SpireReturn<Void> Insert(DialogRelicButton __instance, SpriteBatch sb)
        {
            AbstractRelic relic = _relicPreview.Get(__instance);
            if (relic instanceof PCLRelic) {
                PCLCardTooltip.QueueTooltips((PCLRelic)relic);
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "queuePowerTips");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
