package patches.screens;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.ui.GridCardSelectScreenPatch;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GridCardSelectScreenPatches
{
    public static ArrayList<AbstractCard> cardList = new ArrayList();
    public static Field hoveredCardField;

    public static AbstractCard getHoveredCard() {
        GridCardSelectScreen gc = AbstractDungeon.gridSelectScreen;

        try {
            if (hoveredCardField == null) {
                hoveredCardField = gc.getClass().getDeclaredField("hoveredCard");
            }

            hoveredCardField.setAccessible(true);
            return (AbstractCard)hoveredCardField.get(gc);
        } catch (Exception var2) {
            System.out.println("Exception occurred when getting private field hoveredCard from StSLib: " + var2.toString());
            return null;
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="calculateScrollBounds")
    public static class GridCardSelectScreen_CalculateScrollBounds
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GridCardSelectScreen __instance)
        {
            if (GridCardSelectScreenPatch.CalculateScrollBounds(__instance))
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="callOnOpen")
    public static class GridCardSelectScreen_CallOnOpen
    {
        @SpirePostfixPatch
        public static void Postfix(GridCardSelectScreen __instance)
        {
            GridCardSelectScreenPatch.Open(__instance);
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="updateCardPositionsAndHoverLogic")
    public static class GridCardSelectScreen_UpdateCardPositionsAndHoverLogic
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GridCardSelectScreen __instance)
        {
            if (GridCardSelectScreenPatch.UpdateCardPositionAndHover(__instance))
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class ConfirmUpgrade {
        public ConfirmUpgrade() {
        }

        @SpireInsertPatch(
                locator = GridCardSelectScreenPatches.ConfirmUpgrade.Locator.class
        )
        public static void Insert(GridCardSelectScreen __instance) {
            AbstractCard hoveredCard = GridCardSelectScreenPatches.getHoveredCard();
            if (hoveredCard instanceof EYBCard) {
                if ((Boolean)GridCardSelectScreenPatches.BranchSelectFields.isBranchUpgrading.get(__instance)) {
                    ((EYBCard) hoveredCard).SetForm(1, hoveredCard.timesUpgraded);
                } else {
                    ((EYBCard) hoveredCard).SetForm(0, hoveredCard.timesUpgraded);
                }

                GridCardSelectScreenPatches.BranchSelectFields.isBranchUpgrading.set(__instance, false);
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 1]};
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "cancelUpgrade"
    )
    public static class CancelUpgrade {
        public CancelUpgrade() {
        }

        public static void Prefix(GridCardSelectScreen __instance) {
            GridCardSelectScreenPatches.BranchSelectFields.waitingForBranchUpgradeSelection.set(__instance, false);
            GridCardSelectScreenPatches.BranchSelectFields.isBranchUpgrading.set(__instance, false);
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "renderArrows"
    )
    public static class RenderSplitArrows {

        private static float arrowScale1 = 1.0F;
        private static float arrowScale2 = 1.0F;
        private static float arrowScale3 = 1.0F;
        private static float arrowTimer = 0.0F;

        public RenderSplitArrows() {
        }

        @SpirePrefixPatch
        public static SpireReturn Prefix(GridCardSelectScreen __instance, SpriteBatch sb)
        {
            AbstractCard c = GridCardSelectScreenPatches.getHoveredCard();
            if (__instance.forUpgrade && c instanceof EYBCard && ((EYBCard) c).cardData.CanToggleOnUpgrade)
            {
                float x = (float)Settings.WIDTH / 2.0F - 73.0F * Settings.scale - 32.0F;
                float y = (float)Settings.HEIGHT / 2.0F - 32.0F;
                float by = 64 * Settings.scale;
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y + by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, 45.0F, 0, 0, 64, 64, false, false);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y - by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, -45.0F, 0, 0, 64, 64, false, false);
                x += 64.0F * Settings.scale;
                by += 64.0F * Settings.scale;
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y + by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, 45.0F, 0, 0, 64, 64, false, false);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y - by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, -45.0F, 0, 0, 64, 64, false, false);
                x += 64.0F * Settings.scale;
                by += 64.0F * Settings.scale;
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y + by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, 45.0F, 0, 0, 64, 64, false, false);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y - by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, -45.0F, 0, 0, 64, 64, false, false);
                arrowTimer += Gdx.graphics.getDeltaTime() * 2.0F;
                arrowScale1 = 0.8F + (MathUtils.cos(arrowTimer) + 1.0F) / 8.0F;
                arrowScale2 = 0.8F + (MathUtils.cos(arrowTimer - 0.8F) + 1.0F) / 8.0F;
                arrowScale3 = 0.8F + (MathUtils.cos(arrowTimer - 1.6F) + 1.0F) / 8.0F;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();


        }
    }

    @SpirePatch(
            clz = GridSelectConfirmButton.class,
            method = "render"
    )
    public static class BranchUpgradeConfirm {
        public BranchUpgradeConfirm() {
        }

        public static SpireReturn Prefix(GridSelectConfirmButton __instance, SpriteBatch sb) {
            AbstractCard c = GridCardSelectScreenPatches.getHoveredCard();
            return (Boolean)GridCardSelectScreenPatches.BranchSelectFields.waitingForBranchUpgradeSelection.get(AbstractDungeon.gridSelectScreen) && c instanceof EYBCard && ((EYBCard) c).cardData.CanToggleOnUpgrade ? SpireReturn.Return((Object)null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "render"
    )
    public static class RenderBranchingUpgrade {
        public RenderBranchingUpgrade() {
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderArrows")) {
                        m.replace("$_ = $proceed($$);if (" + GridCardSelectScreenPatches.RenderBranchingUpgrade.class.getName() + ".Do(this, sb).isPresent()) {return;}");
                    }

                }
            };
        }

        public static SpireReturn Do(GridCardSelectScreen __instance, SpriteBatch sb) {
            AbstractCard c = GridCardSelectScreenPatches.getHoveredCard();
            if (__instance.forUpgrade && c instanceof EYBCard && ((EYBCard) c).cardData.CanToggleOnUpgrade) {
                GridCardSelectScreenPatches.cardList.clear();
                AbstractCard branchUpgradedCard = (AbstractCard)GridCardSelectScreenPatches.BranchSelectFields.branchUpgradePreviewCard.get(__instance);
                c.current_x = (float)Settings.WIDTH * 0.36F;
                c.current_y = (float)Settings.HEIGHT / 2.0F;
                c.target_x = (float)Settings.WIDTH * 0.36F;
                c.target_y = (float)Settings.HEIGHT / 2.0F;
                c.render(sb);
                c.updateHoverLogic();
                c.hb.resize(0.0F, 0.0F);
                if (__instance.upgradePreviewCard.hb.hovered) {
                    __instance.upgradePreviewCard.drawScale = 1.0F;
                } else {
                    __instance.upgradePreviewCard.drawScale = 0.9F;
                }

                __instance.upgradePreviewCard.current_x = (float)Settings.WIDTH * 0.63F;
                __instance.upgradePreviewCard.current_y = (float)Settings.HEIGHT * 0.75F - 50.0F * Settings.scale;
                __instance.upgradePreviewCard.target_x = (float)Settings.WIDTH * 0.63F;
                __instance.upgradePreviewCard.target_y = (float)Settings.HEIGHT * 0.75F - 50.0F * Settings.scale;
                __instance.upgradePreviewCard.render(sb);
                __instance.upgradePreviewCard.updateHoverLogic();
                __instance.upgradePreviewCard.renderCardTip(sb);
                GridCardSelectScreenPatches.cardList.add(__instance.upgradePreviewCard);
                if (branchUpgradedCard.hb.hovered) {
                    branchUpgradedCard.drawScale = 1.0F;
                } else {
                    branchUpgradedCard.drawScale = 0.9F;
                }

                branchUpgradedCard.current_x = (float)Settings.WIDTH * 0.63F;
                branchUpgradedCard.current_y = (float)Settings.HEIGHT / 4.0F + 50.0F * Settings.scale;
                branchUpgradedCard.target_x = (float)Settings.WIDTH * 0.63F;
                branchUpgradedCard.target_y = (float)Settings.HEIGHT / 4.0F + 50.0F * Settings.scale;
                branchUpgradedCard.render(sb);
                branchUpgradedCard.updateHoverLogic();
                branchUpgradedCard.renderCardTip(sb);
                GridCardSelectScreenPatches.cardList.add(branchUpgradedCard);
                if (__instance.forUpgrade || __instance.forTransform || __instance.forPurge || __instance.isJustForConfirming || __instance.anyNumber) {
                    __instance.confirmButton.render(sb);
                }

                CardGroup targetGroup = (CardGroup) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "targetGroup");
                String tipMsg = (String)ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "tipMsg");
                if (!__instance.isJustForConfirming || targetGroup.size() > 5) {
                    FontHelper.renderDeckViewTip(sb, tipMsg, 96.0F * Settings.scale, Settings.CREAM_COLOR);
                }

                return SpireReturn.Return((Object)null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class StupidFuckingUpdateBullshitImSoMadDontChangeThisClassNameKio {
        public StupidFuckingUpdateBullshitImSoMadDontChangeThisClassNameKio() {
        }

        @SpireInsertPatch(
                locator = GridCardSelectScreenPatches.StupidFuckingUpdateBullshitImSoMadDontChangeThisClassNameKio.Locator.class
        )
        public static void Insert(GridCardSelectScreen __instance) {
            if (GridCardSelectScreenPatches.BranchSelectFields.branchUpgradePreviewCard.get(__instance) != null) {
                ((AbstractCard)GridCardSelectScreenPatches.BranchSelectFields.branchUpgradePreviewCard.get(__instance)).update();
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "update");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]};
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class GetBranchingUpgrade {
        public GetBranchingUpgrade() {
        }

        @SpireInsertPatch(
                locator = GridCardSelectScreenPatches.GetBranchingUpgrade.Locator.class
        )
        public static void Insert(GridCardSelectScreen __instance) {
            AbstractCard c = GridCardSelectScreenPatches.getHoveredCard();
            if (c instanceof EYBCard && ((EYBCard) c).cardData.CanToggleOnUpgrade) {
                AbstractCard previewCard = c.makeStatEquivalentCopy();
                ((EYBCard)previewCard).SetForm(1, previewCard.timesUpgraded);
                previewCard.upgrade();
                previewCard.displayUpgrades();
                GridCardSelectScreenPatches.BranchSelectFields.branchUpgradePreviewCard.set(__instance, previewCard);
                GridCardSelectScreenPatches.BranchSelectFields.waitingForBranchUpgradeSelection.set(__instance, true);
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "makeStatEquivalentCopy");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "<class>"
    )
    public static class BranchSelectFields {
        public static SpireField<AbstractCard> branchUpgradePreviewCard = new SpireField(() -> {
            return null;
        });
        public static SpireField<Boolean> waitingForBranchUpgradeSelection = new SpireField(() -> {
            return false;
        });
        public static SpireField<Boolean> isBranchUpgrading = new SpireField(() -> {
            return false;
        });

        public BranchSelectFields() {
        }
    }
}
