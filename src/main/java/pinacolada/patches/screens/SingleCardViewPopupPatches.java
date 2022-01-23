package pinacolada.patches.screens;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.ui.common.PCLSingleCardPopup;
import pinacolada.utilities.PCLJUtils;

public class SingleCardViewPopupPatches
{
    private static final FieldInfo<AbstractCard> _card = PCLJUtils.GetField("card", SingleCardViewPopup.class);
    private static final PCLImages Images = GR.PCL.Images;
    private static final PCLSingleCardPopup betterPopup = new PCLSingleCardPopup();

    @SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class})
    public static class SingleCardViewPopup_Open
    {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(SingleCardViewPopup __instance, AbstractCard card)
        {
            PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
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
            PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
            if (c != null && !c.isFlipped)
            {
                GR.UI.CardPopup.Open(c, group);

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
