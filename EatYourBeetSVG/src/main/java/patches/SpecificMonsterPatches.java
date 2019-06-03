package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class SpecificMonsterPatches
{
    @SpirePatch(clz = Lagavulin.class, method = "die")
    public static class CardCrawlGamePatches_loadPlayerSave
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Lagavulin lagavulin)
        {
            int lagavulinCount = 0;
            int aliveLagavulinCount = 0;

            ArrayList<AbstractMonster> monsters = PlayerStatistics.GetCurrentEnemies(false);
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