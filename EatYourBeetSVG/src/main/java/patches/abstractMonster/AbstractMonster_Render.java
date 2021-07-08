package patches.abstractMonster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;

@SpirePatch(clz= AbstractMonster.class, method = "renderDamageRange", paramtypez = {SpriteBatch.class})
public class AbstractMonster_Render
{
    @SpirePrefixPatch
    public static void Method(AbstractMonster __instance, SpriteBatch sb)
    {
        GR.UI.CombatScreen.Intents.RenderMonsterInfo(__instance, sb);
    }
}