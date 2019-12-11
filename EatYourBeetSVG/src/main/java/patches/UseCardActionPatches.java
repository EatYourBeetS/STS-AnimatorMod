package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.unnamed.MoveToVoidAction;
import eatyourbeets.utilities.GameActions;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;

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
                GameActions.Bottom.Add(new MoveToVoidAction(card));
                shouldPurge = true;
            }

            return shouldPurge;
        }
    }
}

