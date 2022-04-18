package patches.combatRewardScreen;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.events.animator.NoCardsInRewardEvent;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.rooms.AnimatorCustomEliteRoom;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;
import javassist.CtBehavior;

import java.util.ArrayList;

public class CombatRewardScreenPatches
{
    @SpirePatch(clz = CombatRewardScreen.class, method = "update")
    public static class CombatRewardScreenPatches_Update
    {
        private static final MethodInfo.T0<?> _updateEffects = JUtils.GetMethod("updateEffects", CombatRewardScreen.class);

        @SpirePrefixPatch
        public static SpireReturn Prefix(CombatRewardScreen __instance)
        {
            if (GameEffects.IsEmpty())
            {
                return SpireReturn.Continue();
            }

            _updateEffects.Invoke(__instance);
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class CombatRewardScreenPatches_SetupItemReward
    {
        @SpirePrefixPatch
        public static void Prefix(CombatRewardScreen __instance)
        {
            final AnimatorCustomEliteRoom room = JUtils.SafeCast(GameUtilities.GetCurrentRoom(false), AnimatorCustomEliteRoom.class);
            if (room != null && room.removeNormalRewards)
            {
                room.event = new NoCardsInRewardEvent(true);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CombatRewardScreen __instance)
        {
            boolean normalRewards = GameUtilities.AreRewardsAllowed(true);

            final AbstractPlayer p = AbstractDungeon.player;
            for (AbstractRelic r : p.relics)
            {
                if (r instanceof OnReceiveRewardsListener)
                {
                    ((OnReceiveRewardsListener) r).OnReceiveRewards(__instance.rewards, normalRewards);
                }
            }

            final ArrayList<String> eybCardIDs = new ArrayList<>();
            for (AbstractCard c : p.masterDeck.group)
            {
                if (c instanceof OnReceiveRewardsListener)
                {
                    ((OnReceiveRewardsListener) c).OnReceiveRewards(__instance.rewards, normalRewards);
                }
                if (c instanceof EYBCard && !eybCardIDs.contains(c.cardID))
                {
                    ((EYBCard)c).cardData.OnReceiveRewards(__instance.rewards, normalRewards);
                    eybCardIDs.add(c.cardID);
                }
            }

            if (p instanceof OnReceiveRewardsListener)
            {
                ((OnReceiveRewardsListener) p).OnReceiveRewards(__instance.rewards, normalRewards);
            }

            final AnimatorCustomEliteRoom room = JUtils.SafeCast(GameUtilities.GetCurrentRoom(false), AnimatorCustomEliteRoom.class);
            if (room != null && room.removeNormalRewards)
            {
                room.event = null;
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[0]};
            }
        }
    }
}