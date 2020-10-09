package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.resources.GR;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;

@SpirePatch(clz = UseCardAction.class, method = "update")
public class UseCardAction_Update
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor()
        {
            public void edit(javassist.expr.FieldAccess m) throws CannotCompileException
            {
                if (m.getClassName().equals(AbstractCard.class.getName()) && m.getFieldName().equals("purgeOnUse"))
                {
                    m.replace("{ $_ = patches.actions.UseCardAction_Update.Patch($0); }");
                }
            }
        };
    }

    public static boolean Patch(AbstractCard card)
    {
        return card.purgeOnUse || card.tags.contains(GR.Enums.CardTags.PURGE);
    }
}

