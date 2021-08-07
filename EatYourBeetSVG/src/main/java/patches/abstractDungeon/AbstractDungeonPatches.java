package patches.abstractDungeon;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.curse.Curse_AscendersBane;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterInfo;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

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
            AbstractEvent event = EYBEvent.GenerateSpecialEvent(CardCrawlGame.dungeon, rng, GameUtilities.IsPlayerClass(GR.Animator.PlayerClass));
            if (event != null)
            {
                return SpireReturn.Return(event);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion", paramtypez = {AbstractPotion.PotionRarity.class, boolean.class})
    public static class AbstractDungeon_ReturnRandomPotion
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

    @SpirePatch(clz = AbstractDungeon.class, method = "returnEndRandomRelicKey")
    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomRelicKey")
    public static class AbstractDungeonPatches_ReturnRandomRelicKey
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

            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
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
            if (!GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
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
}