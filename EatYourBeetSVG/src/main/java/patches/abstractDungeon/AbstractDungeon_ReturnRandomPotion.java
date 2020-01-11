package patches.abstractDungeon;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;

@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion", paramtypez = {AbstractPotion.PotionRarity.class, boolean.class})
public class AbstractDungeon_ReturnRandomPotion
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractPotion> Prefix(AbstractPotion.PotionRarity rarity, boolean limited)
    {
        if (UnnamedReignRelic.IsEquipped())
        {
            return SpireReturn.Return(new FalseLifePotion());
        }

        return SpireReturn.Continue();
    }
}