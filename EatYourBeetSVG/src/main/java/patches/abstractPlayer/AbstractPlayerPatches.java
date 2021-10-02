package patches.abstractPlayer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.dailymods.SeriesDeck;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class AbstractPlayerPatches
{
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class AbstractPlayer_UseCard
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use"))
                    {
                        m.replace("{ patches.abstractPlayer.AbstractPlayerPatches.AbstractPlayer_UseCard.Use($0, $1, $2); }");
                    }
                }
            };
        }

        public static void Use(AbstractCard c, AbstractPlayer p, AbstractMonster m)
        {
            CombatStats.OnUsingCard(c, p, m);
        }
    }

    @SpirePatch(clz= AbstractPlayer.class, method = "updateInput")
    public static class AbstractPlayer_UpdateInput
    {
        private static final FieldInfo<AbstractMonster> _hoveredMonster = JUtils.GetField("hoveredMonster", AbstractPlayer.class);

        @SpirePostfixPatch
        public static void Method(AbstractPlayer __instance)
        {
            if ((__instance.isDraggingCard || __instance.isHoveringDropZone) && __instance.hoveredCard instanceof EYBCard)
            {
                ((EYBCard)__instance.hoveredCard).OnDrag(_hoveredMonster.Get(__instance));
            }
        }
    }

    @SpirePatch(clz= AbstractPlayer.class, method = "obtainPotion", paramtypez = {int.class, AbstractPotion.class})
    public static class AbstractPlayer_ObtainPotion2
    {
        @SpireInsertPatch(rloc = 0, localvars = {"potionToObtain"})
        public static void Method(AbstractPlayer __instance, int slot, AbstractPotion param, @ByRef AbstractPotion[] potionToObtain)
        {
            if (UnnamedReignRelic.IsEquipped())
            {
                potionToObtain[0] = new FalseLifePotion();
            }
        }
    }

    @SpirePatch(clz= AbstractPlayer.class, method = "obtainPotion", paramtypez = {AbstractPotion.class})
    public static class AbstractPlayer_ObtainPotion
    {
        @SpireInsertPatch(rloc = 0, localvars = {"potionToObtain"})
        public static void Method(AbstractPlayer __instance, AbstractPotion param, @ByRef AbstractPotion[] potionToObtain)
        {
            if (UnnamedReignRelic.IsEquipped())
            {
                potionToObtain[0] = new FalseLifePotion();
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class AbstractPlayer_Damage
    {
        @SpireInsertPatch(localvars = {"damageAmount"}, locator = Locator.class)
        public static void InsertPre(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount)
        {
            damageAmount[0] = Math.max(0, CombatStats.OnModifyDamage(__instance, info, damageAmount[0]));
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class AbstractPlayer_ApplyPreCombatLogic
    {
        @SpirePrefixPatch
        public static void Method(AbstractPlayer __instance)
        {
            CombatStats.OnStartup();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips", paramtypez = {SpriteBatch.class})
    public static class AbstractPlayer_RenderPowerTips
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (EYBCardTooltip.CanRenderTooltips())
            {
                EYBCardTooltip.QueueTooltips(__instance);
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "isCursed")
    public static class AbstractPlayer_IsCursed
    {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Method(AbstractPlayer __instance)
        {
            for (AbstractCard c : __instance.masterDeck.group)
            {
                if (c.type == AbstractCard.CardType.CURSE && c.rarity != AbstractCard.CardRarity.SPECIAL)
                {
                    return SpireReturn.Return(true);
                }
            }

            return SpireReturn.Return(false);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "initializeStarterDeck")
    public static class AbstractPlayer_InitializeStarterDeck
    {
        @SpireInsertPatch(locator = Locator.class,
                          localvars = {"addBaseCards"})
        public static void Insert(AbstractPlayer __instance, @ByRef boolean[] addBaseCards)
        {
            if (ModHelper.isModEnabled(SeriesDeck.ID)) {
                addBaseCards[0] = false;

                ArrayList<AnimatorCard> cards = new ArrayList<>();

                CardSeries series = GR.Animator.Data.SelectedLoadout.Series;
                CardSeries.AddCards(series, CardLibrary.getAllCards(), cards);

                for (AnimatorCard card : cards)
                {
                    if (card.rarity.equals(AbstractCard.CardRarity.COMMON) ||
                            card.rarity.equals(AbstractCard.CardRarity.UNCOMMON) ||
                            card.rarity.equals(AbstractCard.CardRarity.RARE))

                    __instance.masterDeck.addToTop(card.makeCopy());
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
                return new int[]{ LineFinder.findInOrder(ctBehavior, matcher)[0] - 1 };
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class AbstractPlayer_Render
    {
        private static final FieldInfo<Color> _hbTextColor = JUtils.GetField("hbTextColor", AbstractCreature.class);
        private static final FieldInfo<Float> _hbYOffset = JUtils.GetField("hbYOffset", AbstractCreature.class);
        private static final FieldInfo<Float> _targetHealthBarWidth = JUtils.GetField("targetHealthBarWidth", AbstractCreature.class);
        private static final FieldInfo<Float> _healthHideTimer = JUtils.GetField("healthHideTimer", AbstractCreature.class);
        private static final FieldInfo<Float> _HEALTH_BAR_OFFSET_Y = JUtils.GetField("HEALTH_BAR_OFFSET_Y", AbstractCreature.class);
        private static final FieldInfo<Float> _HEALTH_TEXT_OFFSET_Y = JUtils.GetField("HEALTH_TEXT_OFFSET_Y", AbstractCreature.class);

        @SpireInsertPatch(locator = Locator.class)
        public static void InsertPre(AbstractPlayer __instance, SpriteBatch sb)
        {
            if ((_targetHealthBarWidth.Get(__instance) <= 0) || (__instance.currentHealth >= GameActionManager.playerHpLastTurn))
            {
                return;
            }

            final float healthBarOffset_y = _HEALTH_BAR_OFFSET_Y.Get(null);
            final float healthTextOffset_y = _HEALTH_TEXT_OFFSET_Y.Get(null);
            final float y = __instance.hb.cY - (__instance.hb.height / 2.0F) + _hbYOffset.Get(__instance);
            final Color color = _hbTextColor.Get(__instance);
            final float previousAlpha = color.a;

            color.a *= _healthHideTimer.Get(__instance) * 0.7f;
            FontHelper.healthInfoFont.getData().setScale(0.7f);
            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, "(" + GameActionManager.playerHpLastTurn + ")", __instance.hb.cX,
                    y + healthBarOffset_y + (healthTextOffset_y * 4.75f) + (5.0F * Settings.scale), color);
            FontHelper.healthInfoFont.getData().setScale(1f);
            color.a = previousAlpha;
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "renderHealth");
                return new int[]{ LineFinder.findInOrder(ctBehavior, matcher)[0] + 1 };
            }
        }
    }

}
