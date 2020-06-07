//package patches.abstractPlayer;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//
//@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
//public class AbstractPlayer_UseCard
//{
// TODO: Disabled until/if the Unnamed Character is implemented
//
//    public static ExprEditor Instrument()
//    {
//        return new ExprEditor()
//        {
//            public void edit(MethodCall m) throws CannotCompileException
//            {
//                if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use"))
//                {
//                    m.replace("{ " +
//                            "if (patches.AbstractPlayerPatches.AbstractPlayerPatches_useCard.CanUse($0)) " +
//                            "{ " +
//                            "$proceed($$); " +
//                            "} " +
//                            "patches.AbstractPlayerPatches.AbstractPlayerPatches_useCard.AfterUse($0); " +
//                            "}");
//                }
//            }
//        };
//    }
//
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
//}