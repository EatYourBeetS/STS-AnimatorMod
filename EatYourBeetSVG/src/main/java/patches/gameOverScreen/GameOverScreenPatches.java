package patches.gameOverScreen;

/*
  protected void calculateUnlockProgress() {
    this.score = calcScore(isVictory);
    this.unlockLevel = UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass);
    if (this.unlockLevel >= 5) {
      this.maxLevel = true;
      return;
    }
    if (this.score == 0)
      this.playedWhir = true;
    this.unlockProgress = UnlockTracker.getCurrentProgress(AbstractDungeon.player.chosenClass);
    this.unlockTargetStart = this.unlockProgress;
    this.unlockCost = UnlockTracker.getCurrentScoreCost(AbstractDungeon.player.chosenClass);
    this.unlockTargetProgress = this.unlockProgress + this.score;
    this.nextUnlockCost = UnlockTracker.incrementUnlockRamp(this.unlockCost);
    if (this.unlockTargetProgress >= this.unlockCost) {
      this.unlockBundle = UnlockTracker.getUnlockBundle(AbstractDungeon.player.chosenClass, this.unlockLevel);
      if (this.unlockLevel == 4) {
        this.unlockTargetProgress = this.unlockCost;
      } else if (this.unlockTargetProgress > this.unlockCost - this.unlockProgress + this.nextUnlockCost - 1.0F) {
        this.unlockTargetProgress = this.unlockCost - this.unlockProgress + this.nextUnlockCost - 1.0F;
      }
    }
    logger.info("SCOR: " + this.score);
    logger.info("PROG: " + this.unlockProgress);
    logger.info("STRT: " + this.unlockTargetStart);
    logger.info("TRGT: " + this.unlockTargetProgress);
    logger.info("COST: " + this.unlockCost);
    UnlockTracker.addScore(AbstractDungeon.player.chosenClass, this.score);
    this.progressPercent = this.unlockTargetStart / this.unlockCost;
  }

      if (this.maxLevel)
      return;
    this.whiteUiColor.a = this.progressBarAlpha * 0.3F;
    sb.setColor(this.whiteUiColor);
    sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.progressBarX, Settings.HEIGHT * 0.2F, this.progressBarWidth, 14.0F * Settings.scale);
    sb.setColor(new Color(1.0F, 0.8F, 0.3F, this.progressBarAlpha * 0.9F));
    sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.progressBarX, Settings.HEIGHT * 0.2F, this.progressBarWidth * this.progressPercent, 14.0F * Settings.scale);
    sb.setColor(new Color(0.0F, 0.0F, 0.0F, this.progressBarAlpha * 0.25F));
    sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.progressBarX, Settings.HEIGHT * 0.2F, this.progressBarWidth * this.progressPercent, 4.0F * Settings.scale);
    String derp = "[" + (int)this.unlockProgress + "/" + this.unlockCost + "]";
    this.creamUiColor.a = this.progressBarAlpha * 0.9F;
    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, derp, 576.0F * Settings.xScale, Settings.HEIGHT * 0.2F - 12.0F * Settings.scale, this.creamUiColor);
    if (5 - this.unlockLevel == 1) {
      derp = TEXT[42] + (5 - this.unlockLevel);
    } else {
      derp = TEXT[41] + (5 - this.unlockLevel);
    }
    FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, derp, 1344.0F * Settings.xScale, Settings.HEIGHT * 0.2F - 12.0F * Settings.scale, this.creamUiColor);
*/

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class GameOverScreenPatches
{
    public static final int MAX_LEVEL = GR.Animator.Data.MaxUnlockLevel;
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DeathScreen");

    public static final FieldInfo<Integer> _score = JUtils.GetField("score", GameOverScreen.class);
    public static final FieldInfo<Integer> _unlockLevel = JUtils.GetField("unlockLevel", GameOverScreen.class);
    public static final FieldInfo<Integer> _unlockCost = JUtils.GetField("unlockCost", GameOverScreen.class);
    public static final FieldInfo<Integer> _nextUnlockCost = JUtils.GetField("nextUnlockCost", GameOverScreen.class);

    public static final FieldInfo<Float> _unlockProgress = JUtils.GetField("unlockProgress", GameOverScreen.class);
    public static final FieldInfo<Float> _unlockTargetProgress = JUtils.GetField("unlockTargetProgress", GameOverScreen.class);
    public static final FieldInfo<Float> _unlockTargetStart = JUtils.GetField("unlockTargetStart", GameOverScreen.class);
    public static final FieldInfo<Float> _progressPercent = JUtils.GetField("progressPercent", GameOverScreen.class);

    public static final FieldInfo<Float> _statsTimer = JUtils.GetField("statsTimer", GameOverScreen.class);
    public static final FieldInfo<Float> _statAnimateTimer = JUtils.GetField("statAnimateTimer", GameOverScreen.class);
    public static final FieldInfo<Float> _progressBarTimer = JUtils.GetField("progressBarTimer", GameOverScreen.class);
    public static final FieldInfo<Float> _progressBarAlpha = JUtils.GetField("progressBarAlpha", GameOverScreen.class);
    public static final FieldInfo<Float> _progressBarX = JUtils.GetField("progressBarX", GameOverScreen.class);
    public static final FieldInfo<Float> _progressBarWidth = JUtils.GetField("progressBarWidth", GameOverScreen.class);

    public static final FieldInfo<Boolean> _playedWhir = JUtils.GetField("playedWhir", GameOverScreen.class);
    public static final FieldInfo<Boolean> _maxLevel = JUtils.GetField("maxLevel", GameOverScreen.class);
    public static final FieldInfo<Boolean> _showingStats = JUtils.GetField("showingStats", GameOverScreen.class);

    public static final FieldInfo<Color> _fadeBgColor = JUtils.GetField("fadeBgColor", GameOverScreen.class);
    public static final FieldInfo<Color> _whiteUiColor = JUtils.GetField("whiteUiColor", GameOverScreen.class);
    public static final FieldInfo<Color> _creamUiColor= JUtils.GetField("creamUiColor", GameOverScreen.class);

    public static final FieldInfo<ArrayList<AbstractUnlock>> _unlockBundle = JUtils.GetField("unlockBundle", GameOverScreen.class);
    public static final FieldInfo<ArrayList<GameOverStat>> _stats = JUtils.GetField("stats", GameOverScreen.class);

    @SpirePatch(clz = DeathScreen.class, method = "createGameOverStats")
    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class GameOverScreen_createGameOverStats
    {
        private static int ascensionCache;

        @SpirePrefixPatch
        public static void Prefix(Object __instance)
        {
            ascensionCache = AbstractDungeon.ascensionLevel;
            AbstractDungeon.ascensionLevel = GameUtilities.GetActualAscensionLevel();
        }

        @SpirePostfixPatch
        public static void Postfix(Object __instance)
        {
            AbstractDungeon.ascensionLevel = ascensionCache;
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "calcScore", paramtypez = {boolean.class})
    public static class GameOverScreen_calcScore
    {
        private static int ascensionCache;
        private static int playerCache;

        @SpirePrefixPatch
        public static void Prefix(boolean victory)
        {
            ascensionCache = AbstractDungeon.ascensionLevel;
            AbstractDungeon.ascensionLevel = GameUtilities.GetActualAscensionLevel();

            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && !victory && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH)
            {
                final int hashCode = AbstractDungeon.player.hashCode();
                if (playerCache != hashCode)
                {
                    playerCache = hashCode;
                    GR.Animator.Data.RecordDefeat(AbstractDungeon.ascensionLevel);
                }
            }
        }

        @SpirePostfixPatch
        public static void Postfix(boolean victory)
        {
            AbstractDungeon.ascensionLevel = ascensionCache;
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "calculateUnlockProgress")
    public static class GameOverScreen_calculateUnlockProgress
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GameOverScreen __instance)
        {
            if (!GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
            {
                return SpireReturn.Continue();
            }

            _score.Set(__instance, GameOverScreen.calcScore(GameOverScreen.isVictory));
            _unlockLevel.Set(__instance, UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass));

            if (_unlockLevel.Get(__instance) >= MAX_LEVEL)
            {
                _maxLevel.Set(__instance, true);
                return SpireReturn.Return(null);
            }

            if (_score.Get(__instance) == 0)
            {
                _playedWhir.Set(__instance, true);
            }

            _unlockProgress.Set(__instance, (float) UnlockTracker.getCurrentProgress(AbstractDungeon.player.chosenClass));
            _unlockTargetStart.Set(__instance, _unlockProgress.Get(__instance));
            _unlockCost.Set(__instance, UnlockTracker.getCurrentScoreCost(AbstractDungeon.player.chosenClass));
            _unlockTargetProgress.Set(__instance, _unlockProgress.Get(__instance) + _score.Get(__instance));
            _nextUnlockCost.Set(__instance, GR.Animator.GetUnlockCost(1, true));

            if (_unlockTargetProgress.Get(__instance) >= _unlockCost.Get(__instance))
            {
                _unlockBundle.Set(__instance, UnlockTracker.getUnlockBundle(AbstractDungeon.player.chosenClass, _unlockLevel.Get(__instance)));
                if (_unlockLevel.Get(__instance) == (MAX_LEVEL - 1))
                {
                    _unlockTargetProgress.Set(__instance, (float) _unlockCost.Get(__instance));
                }
                else if (_unlockTargetProgress.Get(__instance) > (_unlockCost.Get(__instance) - _unlockProgress.Get(__instance) + _nextUnlockCost.Get(__instance) - 1.0F))
                {
                    _unlockTargetProgress.Set(__instance, (_unlockCost.Get(__instance) - _unlockProgress.Get(__instance) + _nextUnlockCost.Get(__instance) - 1.0F));
                }
            }

            UnlockTracker.addScore(AbstractDungeon.player.chosenClass, _score.Get(__instance));
            _progressPercent.Set(__instance, _unlockTargetStart.Get(__instance) / _unlockCost.Get(__instance));

            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = GameOverScreen.class, method = "renderProgressBar")
    public static class GameOverScreen_renderProgressBar
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GameOverScreen __instance, SpriteBatch sb)
        {
            if (!GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
            {
                return SpireReturn.Continue();
            }
            if (_maxLevel.Get(__instance))
            {
                return SpireReturn.Return(null);
            }

            _whiteUiColor.Get(__instance).a = _progressBarAlpha.Get(__instance) * 0.3f;
            sb.setColor(_whiteUiColor.Get(__instance));

            sb.draw(ImageMaster.WHITE_SQUARE_IMG, _progressBarX.Get(__instance),
                    Settings.HEIGHT * 0.2F, _progressBarWidth.Get(__instance), 14.0F * Settings.scale);
            sb.setColor(new Color(1.0F, 0.8F, 0.3F, _progressBarAlpha.Get(__instance) * 0.9F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, _progressBarX.Get(__instance), Settings.HEIGHT * 0.2F,
                    _progressBarWidth.Get(__instance) * _progressPercent.Get(__instance), 14.0F * Settings.scale);
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, _progressBarAlpha.Get(__instance) * 0.25F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, _progressBarX.Get(__instance), Settings.HEIGHT * 0.2F,
                    _progressBarWidth.Get(__instance) * _progressPercent.Get(__instance), 4.0F * Settings.scale);
            String text = "[" + _unlockProgress.Get(__instance).intValue() + "/" + _unlockCost.Get(__instance) + "]";
            _creamUiColor.Get(__instance).a = _progressBarAlpha.Get(__instance) * 0.9F;
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, text,
                    576.0F * Settings.xScale, Settings.HEIGHT * 0.2F - 12.0F * Settings.scale, _creamUiColor.Get(__instance));

            if (_unlockLevel.Get(__instance) == (MAX_LEVEL - 1))
            {
                text = uiStrings.TEXT[42] + (MAX_LEVEL - _unlockLevel.Get(__instance));
            }
            else
            {
                text = uiStrings.TEXT[41] + (MAX_LEVEL - _unlockLevel.Get(__instance));
            }

            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, text,
                    1344.0F * Settings.xScale, Settings.HEIGHT * 0.2F - 12.0F * Settings.scale, _creamUiColor.Get(__instance));

            return SpireReturn.Return(null);
        }
    }
}