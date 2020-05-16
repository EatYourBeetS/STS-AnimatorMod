package patches;

import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.core.CardCrawlGame.LoadPlayerSaves;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.CtBehavior;

@SpirePatch(
        clz = LoadPlayerSaves.class,
        method = "Postfix"
)
// A patch to make cardmods not dupe themselves on save and load (delete this when the official basemod fix comes out)
public class CardModPatch {
    @SpireInsertPatch(locator = CardModPatch.Locator.class, localvars = {"card"})
    public static void FixJobbysMess(CardCrawlGame _instance, AbstractPlayer p, AbstractCard card) {
        //Cards that give themselves a cardmod in their constructor gets that mod duped when save/continue occurs
        //This is because Jobby saves all cardmods and reapplies them, but doesn't account for cardmods that are
        //"inherent" in the constructor. So we remove all mods before applying the saved mods.
        CardModifierManager.removeAllModifiers(card, true);
        System.out.println("patch runs");
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}