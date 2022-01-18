package pinacolada.patches.abstractDungeon;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterInfo;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import pinacolada.cards.pcl.curse.Curse_AscendersBane;
import pinacolada.dailymods.SeriesDeck;
import pinacolada.events.base.PCLEvent;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;
import java.util.Map;

public class AbstractDungeonPatches
{
    @SpirePatch(clz = AbstractDungeon.class, method = "getEvent", paramtypez = Random.class)
    public static class AbstractDungeonPatches_GetEvent
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractEvent> Prefix(Random rng)
        {
            AbstractEvent event = PCLEvent.GenerateSpecialEvent(CardCrawlGame.dungeon, rng, PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass) || GR.PCL.Config.EnableEventsForOtherCharacters.Get());
            if (event != null)
            {
                return SpireReturn.Return(event);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
    public static class AbstractDungeonPatches_InitializeRelicList
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Prefix(AbstractDungeon __instance)
        {
            if (ModHelper.isModEnabled(SeriesDeck.ID)) {
                AbstractDungeon.relicsToRemoveOnStart.add("Pandora's Box");
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

    @SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
    public static class AbstractDungeonPatches_DungeonTransitionSetup
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            for (EYBMonsterInfo e : EYBMonster.Encounters)
            {
                switch (e.type)
                {
                    case NORMAL:
                    {
                        BaseMod.getMonsterEncounters(e.dungeonID).remove(e.info);
                        BaseMod.getStrongMonsterEncounters(e.dungeonID).remove(e.info);
                        break;
                    }
                    case ELITE:
                    {
                        BaseMod.getEliteEncounters(e.dungeonID).remove(e.info);
                        break;
                    }
                    case BOSS:
                    {
                        BaseMod.getMonsterEncounters(e.dungeonID).remove(e.info);
                        break;
                    }
                }
            }

            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass))
            {
                for (EYBMonsterInfo e : EYBMonster.Encounters)
                {
                    switch (e.type)
                    {
                        case NORMAL:
                        {
                            BaseMod.addStrongMonsterEncounter(e.dungeonID, e.GetInfo());
                            break;
                        }
                        case ELITE:
                        {
                            BaseMod.addEliteEncounter(e.dungeonID, e.GetInfo());
                            break;
                        }
                        case BOSS:
                        {
                            BaseMod.addBoss(e.dungeonID, e.encounterID, e.GetMapIcon(false), e.GetMapIcon(true));
                            break;
                        }
                    }
                }
            }
        }

        @SpirePostfixPatch
        public static void Postfix()
        {
            if (!PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass))
            {
                return;
            }

            final ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
            for (int i = 0; i < cards.size(); i++)
            {
                if (cards.get(i).cardID.equals(AscendersBane.ID))
                {
                    cards.set(i, Curse_AscendersBane.DATA.MakeCopy(false));
                    UnlockTracker.markCardAsSeen(Curse_AscendersBane.DATA.ID);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class AbstractDungeonPatches_CloseCurrentScreen
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN)
            {
                GR.UI.Dispose();
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "addCurseCards")
    public static class AbstractDungeonPatches_AddCurseCards
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix()
        {
            for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet())
            {
                AbstractCard c = entry.getValue();
                if (c.type == AbstractCard.CardType.CURSE && c.rarity != AbstractCard.CardRarity.SPECIAL)
                {
                    AbstractDungeon.curseCardPool.addToTop(c);
                }
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
    public static class AbstractDungeonPatches_OnModifyPower
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("hasPower"))
                    {
                        //onModifyPower checks if the player has focus to update orbs, it doesn't update them if focus is reduced to 0...
                        m.replace("$_ = true;");
                    }
                }
            };
        }
    }

    // The vanilla GetRandomCard from AbstractDungeon does an infinite loop if there are no uncommon and rare power cards in the pool...
    @SpirePatch(clz = AbstractDungeon.class, method = "getCardFromPool")
    public static class AbstractDungeonPatches_GetCardFromPool
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(AbstractCard.CardRarity rarity, AbstractCard.CardType type, boolean useRng)
        {
            return SpireReturn.Return(PCLGameUtilities.GetRandomCard(rarity, type, useRng, true));
        }
    }
}