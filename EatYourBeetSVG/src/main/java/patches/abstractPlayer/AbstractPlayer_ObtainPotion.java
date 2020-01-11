package patches.abstractPlayer;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;

@SpirePatch(clz= AbstractPlayer.class, method = "obtainPotion", paramtypez = {AbstractPotion.class})
public class AbstractPlayer_ObtainPotion
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