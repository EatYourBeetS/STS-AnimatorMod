package pinacolada.patches.screens;

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
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import pinacolada.cards.base.PCLCard;
import pinacolada.ui.GridCardSelectScreenPatch;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GridCardSelectScreenPatches
{
    public static final float[] Y_POSITIONS_2 = new float[]{
            Settings.HEIGHT * 0.75F - 50.0F * Settings.scale,
            Settings.HEIGHT * 0.25F + 50.0F * Settings.scale
    };
    public static final float[] Y_POSITIONS_3 = new float[]{
            Settings.HEIGHT * 0.75F + 25.0F * Settings.scale,
            Settings.HEIGHT * 0.5F,
            Settings.HEIGHT * 0.25F - 25.0F * Settings.scale
    };
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
            System.out.println("Exception occurred when getting private field hoveredCard: " + var2.toString());
            return null;
        }
    }

    public static void renderPreviewCard(SpriteBatch sb, AbstractCard card, float unHoveredScale, float y) {
        card.drawScale = card.hb.hovered ? unHoveredScale + 0.1f : unHoveredScale;
        card.current_x = card.target_x = (float)Settings.WIDTH * 0.63F;
        card.current_y = card.target_y = y;
        card.render(sb);
        card.updateHoverLogic();
        card.renderCardTip(sb);
        GridCardSelectScreenPatches.cardList.add(card);
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
            if (hoveredCard instanceof PCLCard) {
                ((PCLCard) hoveredCard).SetForm(BranchSelectFields.branchUpgradeForm.get(__instance), hoveredCard.timesUpgraded);
                GridCardSelectScreenPatches.BranchSelectFields.branchUpgradeForm.set(__instance, 0);
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
            BranchSelectFields.branchUpgradeForm.set(__instance, 0);
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
            if (__instance.forUpgrade && c instanceof PCLCard && ((PCLCard) c).cardData.CanToggleOnUpgrade)
            {
                float x = (float)Settings.WIDTH / 2.0F - 73.0F * Settings.scale - 32.0F;
                float y = (float)Settings.HEIGHT / 2.0F - 32.0F;
                float by = 64 * Settings.scale;
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y + by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, 45.0F, 0, 0, 64, 64, false, false);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y - by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, -45.0F, 0, 0, 64, 64, false, false);
                x += 64.0F * Settings.scale;
                by += 64.0F * Settings.scale;
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y + by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, 45.0F, 0, 0, 64, 64, false, false);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y - by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, -45.0F, 0, 0, 64, 64, false, false);
                x += 64.0F * Settings.scale;
                by += 64.0F * Settings.scale;
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y + by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, 45.0F, 0, 0, 64, 64, false, false);
                sb.draw(ImageMaster.UPGRADE_ARROW, x, y - by, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, -45.0F, 0, 0, 64, 64, false, false);

                if (GridCardSelectScreenPatches.BranchSelectFields.branchUpgrades.get(__instance).size() > 2) {
                    x = (float)Settings.WIDTH / 2.0F - 73.0F * Settings.scale - 32.0F;
                    sb.draw(ImageMaster.UPGRADE_ARROW, x, y, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, 0, 0, 0, 64, 64, false, false);
                    sb.draw(ImageMaster.UPGRADE_ARROW, x + 64.0F * Settings.scale, y, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, 0, 0, 0, 64, 64, false, false);
                    sb.draw(ImageMaster.UPGRADE_ARROW, x + 128.0F * Settings.scale, y, 32.0F, 32.0F, 64.0F, 64.0F, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, 0, 0, 0, 64, 64, false, false);
                }

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
            return BranchSelectFields.waitingForBranchUpgradeSelection.get(AbstractDungeon.gridSelectScreen) && c instanceof PCLCard && ((PCLCard) c).cardData.CanToggleOnUpgrade ? SpireReturn.Return(null) : SpireReturn.Continue();
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
            if (__instance.forUpgrade && c instanceof PCLCard && ((PCLCard) c).cardData.CanToggleOnUpgrade) {
                GridCardSelectScreenPatches.cardList.clear();
                c.current_x = c.target_x = (float)Settings.WIDTH * 0.36F;
                c.current_y = c.target_y = (float)Settings.HEIGHT / 2.0F;
                c.render(sb);
                c.updateHoverLogic();
                c.hb.resize(0.0F, 0.0F);

                final ArrayList<AbstractCard> list = GridCardSelectScreenPatches.BranchSelectFields.branchUpgrades.get(__instance);
                int size = list.size();
                final float scale = size == 2 ? 0.9f : 0.62f;
                final float[] yIndices = size == 2 ? Y_POSITIONS_2 : Y_POSITIONS_3;
                for (int i = 0; i < size; i++) {
                    if (yIndices.length > i) {
                        renderPreviewCard(sb, list.get(i), scale, yIndices[i]);
                    }
                }

                if (__instance.forUpgrade || __instance.forTransform || __instance.forPurge || __instance.isJustForConfirming || __instance.anyNumber) {
                    __instance.confirmButton.render(sb);
                }

                CardGroup targetGroup = ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "targetGroup");
                String tipMsg = ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "tipMsg");
                if (!__instance.isJustForConfirming || targetGroup.size() > 5) {
                    FontHelper.renderDeckViewTip(sb, tipMsg, 96.0F * Settings.scale, Settings.CREAM_COLOR);
                }

                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class GridCardSelectScreen_Update {
        public GridCardSelectScreen_Update() {
        }

        @SpireInsertPatch(
                locator = GridCardSelectScreen_Update.Locator.class
        )
        public static void Insert(GridCardSelectScreen __instance) {
            for (AbstractCard c : GridCardSelectScreenPatches.BranchSelectFields.branchUpgrades.get(__instance)) {
                c.update();
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
            PCLCard pC = PCLJUtils.SafeCast(c, PCLCard.class);
            if (pC != null && pC.cardData.CanToggleOnUpgrade) {
                final ArrayList<AbstractCard> list = GridCardSelectScreenPatches.BranchSelectFields.branchUpgrades.get(__instance);
                list.clear();

                for (int i = 0; i < pC.cardData.MaxForms; i++) {
                    PCLCard previewCard = (PCLCard) pC.makeStatEquivalentCopy();
                    previewCard.SetForm(i, previewCard.timesUpgraded);
                    previewCard.upgrade();
                    previewCard.displayUpgrades();
                    previewCard.initializeDescription();
                    list.add(previewCard);
                }

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
        public static SpireField<ArrayList<AbstractCard>> branchUpgrades = new SpireField(ArrayList::new);
        public static SpireField<Boolean> waitingForBranchUpgradeSelection = new SpireField(() -> {
            return false;
        });
        public static SpireField<Integer> branchUpgradeForm = new SpireField(() -> {
            return 0;
        });

        public BranchSelectFields() {
        }
    }
}
