package patches.unlockTracker;

/*
public static void addScore(PlayerClass c, int scoreGained)
{
        String key_unlock_level = c.toString() + "UnlockLevel";
        String key_progress = c.toString() + "Progress";
        String key_current_cost = c.toString() + "CurrentCost";
        String key_total_score = c.toString() + "TotalScore";
        String key_high_score = c.toString() + "HighScore";
        logger.info("Keys");
        logger.info(key_unlock_level);
        logger.info(key_progress);
        logger.info(key_current_cost);
        logger.info(key_total_score);
        logger.info(key_high_score);
        int p = unlockProgress.getInteger(key_progress, 0);
        p += scoreGained;
        int total;
        int highscore;
        if (p >= unlockProgress.getInteger(key_current_cost, 300)) {
            logger.info("[DEBUG] Level up!");
            total = unlockProgress.getInteger(key_unlock_level, 0);
            ++total;
            unlockProgress.putInteger(key_unlock_level, total);
            p -= unlockProgress.getInteger(key_current_cost, 300);
            unlockProgress.putInteger(key_progress, p);
            logger.info("[DEBUG] Score Progress: " + key_progress);
            highscore = unlockProgress.getInteger(key_current_cost, 300);
            unlockProgress.putInteger(key_current_cost, incrementUnlockRamp(highscore));
            if (p > unlockProgress.getInteger(key_current_cost, 300)) {
                unlockProgress.putInteger(key_progress, unlockProgress.getInteger(key_current_cost, 300) - 1);
                logger.info("Overfloat maxes out next level");
            }
        } else {
            unlockProgress.putInteger(key_progress, p);
        }

        total = unlockProgress.getInteger(key_total_score, 0);
        total += scoreGained;
        unlockProgress.putInteger(key_total_score, total);
        logger.info("[DEBUG] Total score: " + total);
        highscore = unlockProgress.getInteger(key_high_score, 0);
        if (scoreGained > highscore) {
            unlockProgress.putInteger(key_high_score, scoreGained);
            logger.info("[DEBUG] New high score: " + scoreGained);
        }

        unlockProgress.flush();
    }
*/

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class UnlockTrackerPatches
{
    @SpirePatch(clz = UnlockTracker.class, method = "addScore", paramtypez = {AbstractPlayer.PlayerClass.class, int.class})
    public static class UnlockTracker_addScore
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractPlayer.PlayerClass c, int scoreGained)
        {
            if (c != GR.Animator.PlayerClass)
            {
                return SpireReturn.Continue();
            }

            String key_unlock_level = c.toString() + "UnlockLevel";
            String key_progress = c.toString() + "Progress";
            String key_current_cost = c.toString() + "CurrentCost";
            String key_total_score = c.toString() + "TotalScore";
            String key_high_score = c.toString() + "HighScore";
            JUtils.LogInfo(UnlockTrackerPatches.class, "Keys");
            JUtils.LogInfo(UnlockTrackerPatches.class, key_unlock_level);
            JUtils.LogInfo(UnlockTrackerPatches.class, key_progress);
            JUtils.LogInfo(UnlockTrackerPatches.class, key_current_cost);
            JUtils.LogInfo(UnlockTrackerPatches.class, key_total_score);
            JUtils.LogInfo(UnlockTrackerPatches.class, key_high_score);
            int p = UnlockTracker.unlockProgress.getInteger(key_progress, 0);
            p += scoreGained;
            int total;
            int highscore;
            int unlockCost = GR.Animator.GetUnlockCost();
            if (p >= unlockCost)
            {
                JUtils.LogInfo(UnlockTrackerPatches.class, "[DEBUG] Level up!");
                total = UnlockTracker.unlockProgress.getInteger(key_unlock_level, 0);
                ++total;
                UnlockTracker.unlockProgress.putInteger(key_unlock_level, total); // <------- LEVEL UP
                p -= UnlockTracker.unlockProgress.getInteger(key_current_cost, unlockCost);
                UnlockTracker.unlockProgress.putInteger(key_progress, p);
                JUtils.LogInfo(UnlockTrackerPatches.class, "[DEBUG] Score Progress: " + key_progress);
                //highscore = UnlockTracker.unlockProgress.getInteger(key_current_cost, defaultCost);
                int nextUnlockCost = GR.Animator.GetUnlockCost();
                UnlockTracker.unlockProgress.putInteger(key_current_cost, nextUnlockCost);
                if (p > nextUnlockCost)
                {
                    UnlockTracker.unlockProgress.putInteger(key_progress, nextUnlockCost - 1);
                    JUtils.LogInfo(UnlockTrackerPatches.class, "Overflow maxes out next level");
                }
            }
            else
            {
                UnlockTracker.unlockProgress.putInteger(key_progress, p);
            }

            total = UnlockTracker.unlockProgress.getInteger(key_total_score, 0);
            total += scoreGained;
            UnlockTracker.unlockProgress.putInteger(key_total_score, total);
            JUtils.LogInfo(UnlockTrackerPatches.class, "[DEBUG] Total score: " + total);
            highscore = UnlockTracker.unlockProgress.getInteger(key_high_score, 0);
            if (scoreGained > highscore)
            {
                UnlockTracker.unlockProgress.putInteger(key_high_score, scoreGained);
                JUtils.LogInfo(UnlockTrackerPatches.class, "[DEBUG] New high score: " + scoreGained);
            }

            UnlockTracker.unlockProgress.flush();

            return SpireReturn.Return(null);
        }
    }
}
