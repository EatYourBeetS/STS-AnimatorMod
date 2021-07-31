package patches.abstractPlayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

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
        @SpireInsertPatch(localvars = {"damageAmount"}, locator = patches.abstractPlayer.AbstractPlayerPatches.AbstractPlayer_Damage.Locator.class)
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
}
