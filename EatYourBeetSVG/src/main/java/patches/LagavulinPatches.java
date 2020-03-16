package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import eatyourbeets.actions.special.PlayTempBgmAction;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

// Fix Lagavulin music playing multiple times when fighting multiple Lagavulin (in act 5)
public class LagavulinPatches
{
    @SpirePatch(clz = Lagavulin.class, method = "changeState")
    public static class Lagavulin_ChangeState
    {
        @SpirePostfixPatch
        public static void Prefix(Lagavulin lagavulin, String stateName)
        {
            if (stateName.equals("OPEN") && !lagavulin.isDying)
            {
                GameActions.Bottom.Add(new PlayTempBgmAction("ELITE"));
            }
        }
    }

    @SpirePatch(clz = Lagavulin.class, method = "die")
    public static class Lagavulin_Die
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Lagavulin lagavulin)
        {
            int lagavulinCount = 0;
            int aliveLagavulinCount = 0;

            ArrayList<AbstractMonster> monsters = GameUtilities.GetAllEnemies(false);
            for (AbstractMonster m : monsters)
            {
                if (m instanceof Lagavulin)
                {
                    lagavulinCount += 1;
                    if (!m.isDeadOrEscaped())
                    {
                        aliveLagavulinCount += 1;
                    }
                }
            }

            if (lagavulinCount > 1)
            {
                lagavulin.die(true);

                if (aliveLagavulinCount == 1)
                {
                    AbstractDungeon.scene.fadeInAmbiance();
                    CardCrawlGame.music.fadeOutTempBGM();
                    CardCrawlGame.music.unsilenceBGM();
                    CardCrawlGame.music.changeBGM("TheBeyond");
                }

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}