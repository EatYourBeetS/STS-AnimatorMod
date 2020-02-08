package patches.screens;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.ui.common.EYBSingleCardPopup;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

public class SingleCardViewPopupPatches
{
    private static final FieldInfo<AbstractCard> _card = JavaUtilities.GetField("card", SingleCardViewPopup.class);
    private static final AnimatorImages Images = GR.Animator.Images;
    private static final EYBSingleCardPopup betterPopup = new EYBSingleCardPopup();

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class})
    public static class SingleCardViewPopup_Open
    {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(SingleCardViewPopup __instance, AbstractCard card)
        {
            EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
            if (c != null && !c.isFlipped)
            {
                GR.UI.CardPopup.Open(c, null);

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class, CardGroup.class})
    public static class SingleCardViewPopup_Open2
    {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(SingleCardViewPopup __instance, AbstractCard card, CardGroup group)
        {
            EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
            if (c != null && !c.isFlipped)
            {
                GR.UI.CardPopup.Open(c, group);

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
