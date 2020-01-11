package patches.potionHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;

@SpirePatch(clz = PotionHelper.class, method = "getPotion", paramtypez = {String.class})
public class PotionHelper_GetPotion
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractPotion> Method(String name)
    {
        if (name.equals(FalseLifePotion.POTION_ID) || (UnnamedReignRelic.IsEquipped() && !name.equals(PotionSlot.POTION_ID)))
        {
            return SpireReturn.Return(new FalseLifePotion());
        }
        else
        {
            return SpireReturn.Continue();
        }
    }
}