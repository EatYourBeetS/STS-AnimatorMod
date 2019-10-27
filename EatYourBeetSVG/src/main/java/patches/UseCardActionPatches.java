package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;

import java.util.ArrayList;

public class UseCardActionPatches
{
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardActionPatches_Update
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException
                {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getFieldName().equals("purgeOnUse"))
                    {
                        m.replace("{ $_ = patches.UseCardActionPatches.UseCardActionPatches_Update.Patch($0); }");
                    }
                }
            };
        }

        public static boolean Patch(AbstractCard card)
        {
            boolean shouldPurge = card.purgeOnUse || card.tags.contains(AbstractEnums.CardTags.PURGE);

            if (!shouldPurge && card.tags.contains(AbstractEnums.CardTags.VOIDBOUND))
            {
                GameActionsHelper.MoveToVoid(card);
                shouldPurge = true;
            }

            return shouldPurge;
        }
    }

//    @SpirePatch(clz = UseCardAction.class, method = "update")
//    public static class UseCardAction_Update
//    {
//        private static Field<AbstractCard> cardField = Utilities.GetPrivateField("targetCard", UseCardAction.class);
//
//        @SpirePostfixPatch
//        public static void Postfix(UseCardAction action)
//        {
//            if (action.isDone)
//            {
//                AbstractCard card = cardField.Get(action);
//                if (card != null && card.tags.contains(AbstractEnums.CardTags.PURGE))
//                {
//                    GameActionsHelper.PurgeCard(card);
//                }
//            }
//        }
//    }
}

