package pinacolada.patches.abstractPlayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.utilities.FieldInfo;
import javassist.CtBehavior;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.dailymods.SeriesDeck;
import pinacolada.orbs.pcl.Air;
import pinacolada.orbs.pcl.Chaos;
import pinacolada.orbs.pcl.Earth;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class AbstractPlayerPatches
{
    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
    public static class AbstractPlayer_ChannelOrb
    {
        @SpireInsertPatch(rloc = 7)
        public static void Insert(AbstractPlayer __instance, @ByRef AbstractOrb[] orbToSet)
        {
            // Convert Orbs specific to Animator/PCL into their PCL/Animator equivalents
            AbstractOrb orb = orbToSet[0];
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                if (orb instanceof Aether) {
                    orbToSet[0] = new Air();
                }
                else if (orb instanceof eatyourbeets.orbs.animator.Earth) {
                    orbToSet[0] = new Earth();
                }
                else if (orb instanceof eatyourbeets.orbs.animator.Fire) {
                    orbToSet[0] = new Fire();
                }
                else if (orb instanceof eatyourbeets.orbs.animator.Chaos) {
                    orbToSet[0] = new Chaos();
                }
            }
            else if (PCLGameUtilities.IsPlayerClass(eatyourbeets.resources.GR.Animator.PlayerClass)) {
                if (orb instanceof Air) {
                    orbToSet[0] = new Aether();
                }
                else if (orb instanceof Earth) {
                    orbToSet[0] = new eatyourbeets.orbs.animator.Earth();
                }
                else if (orb instanceof Fire) {
                    orbToSet[0] = new eatyourbeets.orbs.animator.Fire();
                }
                else if (orb instanceof Chaos) {
                    orbToSet[0] = new eatyourbeets.orbs.animator.Chaos();
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class AbstractPlayer_UseCard
    {
        @SpireInsertPatch(rloc = 7)
        public static SpireReturn Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse)
        {
            final PCLCard card = PCLJUtils.SafeCast(c, PCLCard.class);
            if (card != null)
            {
                PCLCombatStats.OnUsingCard(card, __instance, monster);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz= AbstractPlayer.class, method = "updateInput")
    public static class AbstractPlayer_UpdateInput
    {
        private static final FieldInfo<AbstractMonster> _hoveredMonster = PCLJUtils.GetField("hoveredMonster", AbstractPlayer.class);

        @SpirePostfixPatch
        public static void Method(AbstractPlayer __instance)
        {
            if ((__instance.isDraggingCard || __instance.isHoveringDropZone) && __instance.hoveredCard instanceof PCLCard)
            {
                ((PCLCard)__instance.hoveredCard).OnDrag(_hoveredMonster.Get(__instance));
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class AbstractPlayer_ApplyPreCombatLogic
    {
        @SpirePrefixPatch
        public static void Method(AbstractPlayer __instance)
        {
            PCLCombatStats.OnStartup();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips", paramtypez = {SpriteBatch.class})
    public static class AbstractPlayer_RenderPowerTips
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (PCLCardTooltip.CanRenderTooltips())
            {
                PCLCardTooltip.QueueTooltips(__instance);
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "initializeStarterDeck")
    public static class AbstractPlayer_InitializeStarterDeck
    {
        @SpireInsertPatch(locator = Locator.class,
                          localvars = {"addBaseCards"})
        public static void Insert(AbstractPlayer __instance, @ByRef boolean[] addBaseCards)
        {
            if (ModHelper.isModEnabled(SeriesDeck.ID)) {
                addBaseCards[0] = false;

                ArrayList<PCLCard> cards = new ArrayList<>();

                CardSeries series = GR.PCL.Data.SelectedLoadout.Series;
                CardSeries.AddCards(series, CardLibrary.getAllCards(), cards);

                for (PCLCard card : cards)
                {
                    if (!card.color.equals(AbstractCard.CardColor.COLORLESS) && (card.rarity.equals(AbstractCard.CardRarity.COMMON) ||
                            card.rarity.equals(AbstractCard.CardRarity.UNCOMMON) ||
                            card.rarity.equals(AbstractCard.CardRarity.RARE)))

                    __instance.masterDeck.addToTop(card.makeCopy());
                }
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

}
