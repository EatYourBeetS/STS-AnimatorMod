package patches.abstractDungeon;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;

@SpirePatch(clz = AbstractDungeon.class, method = "returnEndRandomRelicKey")
@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomRelicKey")
public class AbstractDungeon_ReturnRandomRelicKey
{
    @SpirePrefixPatch
    public static SpireReturn<String> Prefix(AbstractRelic.RelicTier tier)
    {
        if (UnnamedReignRelic.IsEquipped())
        {
            return SpireReturn.Return(AncientMedallion.ID);
        }

        return SpireReturn.Continue();
    }
}