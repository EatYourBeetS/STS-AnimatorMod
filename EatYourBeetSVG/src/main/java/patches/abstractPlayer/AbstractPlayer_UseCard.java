package patches.abstractPlayer;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class AbstractPlayer_UseCard
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor()
        {
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use"))
                {
                    m.replace("{ patches.abstractPlayer.AbstractPlayer_UseCard.Use($0, $1, $2); }");
                }
            }
        };
    }

    public static void Use(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        CombatStats.Instance.OnUsingCard(c, p, m);
    }

//    public static boolean CanUse(AbstractCard c)
//    {
//        UnnamedCard card = JavaUtilities.SafeCast(c, UnnamedCard.class);
//
//        return card == null || !card.isVoidbound() || card.enteredVoid;
//    }
//
//    public static void AfterUse(AbstractCard c)
//    {
//        PlayerStatistics.Void.UseMastery(c);
//    }
}