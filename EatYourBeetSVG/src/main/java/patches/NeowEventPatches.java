package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import eatyourbeets.rewards.neow.EYBNeowReward;
import eatyourbeets.utilities.GameUtilities;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NeowEventPatches
{
    @SpirePatch(clz = NeowEvent.class, method = "miniBlessing")
    public static class NeowEventPatches_miniBlessing
    {
        @SpireInstrumentPatch
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(NewExpr m) throws CannotCompileException
                {
                    if (m.getClassName().equals(NeowReward.class.getName()))
                    {
                        m.replace("$_ = patches.NeowEventPatches.CreateRewardMini($$);");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "blessing")
    public static class NeowEventPatches_blessing
    {
        @SpireInstrumentPatch
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(NewExpr m) throws CannotCompileException
                {
                    if (m.getClassName().equals(NeowReward.class.getName()))
                    {
                        m.replace("$_ = patches.NeowEventPatches.CreateReward($$);");
                    }
                }
            };
        }
    }

    public static NeowReward CreateRewardMini(boolean firstMini)
    {
        return GameUtilities.IsEYBPlayerClass() ? new EYBNeowReward(firstMini) : new NeowReward(firstMini);
    }

    public static NeowReward CreateReward(int category)
    {
        return GameUtilities.IsEYBPlayerClass() ? new EYBNeowReward(category) : new NeowReward(category);
    }
}
