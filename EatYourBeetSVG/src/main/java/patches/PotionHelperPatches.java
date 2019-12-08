package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.UnnamedReign.UnnamedReignRelic;

public class PotionHelperPatches
{
    @SpirePatch(clz = PotionHelper.class, method = "getPotion", paramtypez = {String.class})
    public static class PotionHelper_GetPotion
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

//    @SpirePatch(clz = PotionHelper.class, method = "isAPotion", paramtypez = {String.class})
//    public static class PotionHelper_IsAPotion
//    {
//        @SpirePrefixPatch
//        public static SpireReturn<Boolean> MethodInfo(String name)
//        {
//            if (name.equals(FalseLifePotion.POTION_ID))
//            {
//                return SpireReturn.Return(true);
//            }
//            else
//            {
//                return SpireReturn.Continue();
//            }
//        }
//    }
//
//    @SpirePatch(clz = PotionHelper.class, method = "initialize", paramtypez = {AbstractPlayer.PlayerClass.class})
//    public static class PotionHelper_Initialize
//    {
//        @SpirePrefixPatch
//        public static SpireReturn<AbstractPotion> MethodInfo(AbstractPlayer.PlayerClass chosenClass)
//        {
//            if (chosenClass == AbstractEnums.Characters.THE_ANIMATOR)
//            {
//                PotionHelper.potions.add(FalseLifePotion.POTION_ID);
//            }
//
//            if (UnnamedReignRelic.IsEquipped())
//            {
//                return SpireReturn.Return(new FalseLifePotion());
//            }
//            else
//            {
//                return SpireReturn.Continue();
//            }
//        }
//    }
}