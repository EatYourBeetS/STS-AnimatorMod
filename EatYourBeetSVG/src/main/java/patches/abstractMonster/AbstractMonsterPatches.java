package patches.abstractMonster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.MindControlPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class AbstractMonsterPatches
{
    @SpirePatch(clz = AbstractMonster.class, method = "<class>")
    public static class AbstractMonster_Fields
    {
        public static final SpireField<EnemyIntent> enemyIntent = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class AbstractMonster_Damage
    {
        @SpireInsertPatch(localvars = {"damageAmount"}, locator = Locator.class)
        public static void InsertPre(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount)
        {
            damageAmount[0] = Math.max(0, CombatStats.OnModifyDamage(__instance, info, damageAmount[0]));
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz= AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class AbstractMonster_Die
    {
        @SpirePrefixPatch
        public static void Method(AbstractMonster __instance, boolean triggerRelics)
        {
            if (!__instance.isDying) // to avoid triggering this more than once
            {
                CombatStats.OnEnemyDying(__instance, triggerRelics);
            }
        }
    }

    @SpirePatch(clz= AbstractMonster.class, method = "renderDamageRange", paramtypez = {SpriteBatch.class})
    public static class AbstractMonster_Render
    {
        @SpirePrefixPatch
        public static void Method(AbstractMonster __instance, SpriteBatch sb)
        {
            GR.UI.CombatScreen.Intents.RenderMonsterInfo(__instance, sb);
        }
    }

    @SpirePatch(clz= AbstractMonster.class, method = "renderTip", paramtypez = {SpriteBatch.class})
    public static class AbstractMonster_RenderTip
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractMonster __instance, SpriteBatch sb)
        {
            if (__instance.reticleAlpha == 0)
            {
                EYBCardTooltip.QueueTooltips(__instance);
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class GameActionManager_GetNextAction {
        public GameActionManager_GetNextAction() {
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.monsters.AbstractMonster") && m.getMethodName().equals("takeTurn")) {
                        m.replace("if (m.hasPower(eatyourbeets.powers.animator.ProvokedPower.POWER_ID)) {patches.abstractMonster.AbstractMonsterPatches.GameActionManager_GetNextAction.Provoke(m);} else if (m.hasPower(eatyourbeets.powers.animator.MindControlPower.POWER_ID)) {patches.abstractMonster.AbstractMonsterPatches.GameActionManager_GetNextAction.Use(m);} else {$_ = $proceed($$);}");
                    }

                }
            };
        }

        public static void Provoke(AbstractMonster m)
        {
            ArrayList<DamageInfo> damages = m.damage;
            if (damages == null || damages.isEmpty()) {
                GameActions.Bottom.DealDamage(m, AbstractDungeon.player, 1, DamageInfo.DamageType.NORMAL, AttackEffects.BLUNT_HEAVY);
            }
            else {
                GameActions.Bottom.DealDamage(m, AbstractDungeon.player, m.damage.get(0).base, m.damage.get(0).type, AttackEffects.BLUNT_HEAVY);
            }
        }

        public static void Use(AbstractMonster m)
        {
            boolean canAct = true;
            for (AbstractPower p : m.powers) {
                MindControlPower mp = JUtils.SafeCast(p, MindControlPower.class);
                if (mp != null && mp.active)
                {
                    canAct = canAct & mp.DoActions();
                }
            }
            if (canAct) {
                m.takeTurn();
            }
        }
    }
}
