package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.effects.special.MaskedTravelerTransformCardsEffect;
import eatyourbeets.effects.special.UnnamedRelicEquipEffect;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.VividPicture;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.TheEgnaroPiece;
import eatyourbeets.relics.animator.unnamedReign.TheEruzaStone;
import eatyourbeets.relics.animator.unnamedReign.TheWolleyCore;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class TheMaskedTraveler2 extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheMaskedTraveler2.class);

    private static EYBCardTooltip rewardsDisabledTip;

    private final ArrayList<AbstractRelic> startingRelicsCache = new ArrayList<>();
    private final AbstractRelic relic1 = new TheEruzaStone();
    private final AbstractRelic relic2 = new TheWolleyCore();
    private final AbstractRelic relic3 = new TheEgnaroPiece();

    public TheMaskedTraveler2()
    {
        super(ID, STRINGS, IMAGES.Portal.Path());

        final MapRoomNode node = AbstractDungeon.getCurrMapNode();
        if (node != null && node.room != null)
        {
            node.room.rewardAllowed = false;
        }

        if (rewardsDisabledTip == null)
        {
            rewardsDisabledTip = new EYBCardTooltip("Warning", GR.Animator.Strings.Misc.RewardsDisabled);
        }

        rewardsDisabledTip.canRender = !GameUtilities.IsNormalRun(true);

        RegisterSpecialPhase(new EnterUnnamedReign());
        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Explanation());
        RegisterPhase(2, new Offering());
        ProgressPhase();
    }

    @Override
    public void update()
    {
        super.update();

        if (GR.UI.Elapsed(2f))
        {
            rewardsDisabledTip.canRender = !GameUtilities.IsNormalRun(true);
        }

        if (rewardsDisabledTip.canRender && currentPhase != null && currentPhase.dialog != null)
        {
            for (LargeDialogOptionButton button : currentPhase.dialog.optionList)
            {
                if (button.msg.equals(EYBEvent.COMMON_STRINGS.Continue()))
                {
                    if (button.hb.hovered)
                    {
                        EYBCardTooltip.QueueTooltip(rewardsDisabledTip, button.hb.x + button.hb.width * 0.5f, button.hb.y + button.hb.height * 3f);
                    }

                    break;
                }
            }
        }
    }

    private static class Introduction extends EYBEventPhase<TheMaskedTraveler2, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
            AddLeaveOption();
        }
    }

    private static class Explanation extends EYBEventPhase<TheMaskedTraveler2, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Explanation(UnnamedRelicEquipEffect.CalculateGoldBonus() + 21));
            AddOption(text.ObtainRelicOption(), event.relic1).AddCallback(this::ObtainRelic);
            AddOption(text.ObtainRelicOption(), event.relic2).AddCallback(this::ObtainRelic);
            AddOption(text.ObtainRelicOption(), event.relic3).AddCallback(this::ObtainRelic);
            AddLeaveOption();
        }

        private void ObtainRelic(EYBEventOption option)
        {
            for (AbstractRelic relic : player.relics)
            {
                if (relic.tier == AbstractRelic.RelicTier.STARTER)
                {
                    event.startingRelicsCache.add(relic);
                }
                else if (relic instanceof VividPicture)
                {
                    event.startingRelicsCache.add(new LivingPicture(((VividPicture) relic).enchantment));
                }
            }

            AbstractRelic relic = option.relic.makeCopy();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
            ProgressPhase();
        }
    }

    private static class Offering extends EYBEventPhase<TheMaskedTraveler2, EventStrings>
    {
        private static final AncientMedallion MedallionPreview = new AncientMedallion();
        private static final int REPLACE_CARDS = 2;
        private int currentHPLoss = 0;

        @Override
        protected void OnEnter()
        {
            int hpLossPercentage = 8;
            if (player instanceof AnimatorCharacter)
            {
                hpLossPercentage = 12;
            }

            currentHPLoss = (int) Math.ceil((UnnamedRelicEquipEffect.CalculateMaxHealth() / 100.0) * hpLossPercentage);

            AddText(text.Offering());
            AddOption(text.ObtainRelicOption(), MedallionPreview).AddCallback(this::ObtainRelic);
            AddOption(text.ReplaceCardsOption(REPLACE_CARDS, REPLACE_CARDS)).AddCallback(this::ReplaceCards);
            AddOption(text.RecoverRelicsOption(currentHPLoss)).AddCallback(this::RecoverRelics);
        }

        private void ObtainRelic()
        {
            ClearOptions();
            GameUtilities.ObtainRelic(InputHelper.mX, InputHelper.mY, new AncientMedallion(true));
            AddPhaseChangeOption(COMMON_STRINGS.Leave(), EnterUnnamedReign.class);
            BuildOptions();
        }

        private void ReplaceCards()
        {
            ClearOptions();
            GameEffects.List.Add(new MaskedTravelerTransformCardsEffect(REPLACE_CARDS, REPLACE_CARDS))
            .AddCallback(() -> ChangePhase(EnterUnnamedReign.class));
        }

        private void RecoverRelics()
        {
            ClearOptions();
            player.decreaseMaxHealth(currentHPLoss);

            for (String relicID : player.getStartingRelics())
            {
                if (!RecoverPreviousRelic(relicID))
                {
                    AbstractRelic relic = RelicLibrary.getRelic(relicID).makeCopy();
                    relic.instantObtain();
                }
            }

            event.startingRelicsCache.clear();
            GameEffects.List.Callback(new WaitAction(0.3f))
            .AddCallback(() -> ChangePhase(EnterUnnamedReign.class));
        }

        private boolean RecoverPreviousRelic(String relicID)
        {
            AbstractRelic relic = null;
            for (AbstractRelic previousRelic : event.startingRelicsCache)
            {
                if (relicID.equals(previousRelic.relicId))
                {
                    relic = previousRelic;
                    break;
                }
            }

            if (relic == null)
            {
                return false;
            }

            if (Circlet.ID.equals(relic.relicId) && player.hasRelic(Circlet.ID))
            {
                AbstractRelic circlet = player.getRelic(Circlet.ID);
                ++circlet.counter;
                circlet.flash();
            }
            else
            {
                if (relic instanceof EnchantableRelic)
                {
                    EnchantableRelic r = ((EnchantableRelic) relic);
                    r.ApplyEnchantment(r.GetEnchantmentLevel() >= 2 ? (Enchantment) r.enchantment.makeCopy() : null);
                }

                float START_X = 64f * Settings.scale;
                float START_Y = (float) Settings.HEIGHT - 102f * Settings.scale;

                relic.playLandingSFX();
                relic.isDone = true;
                relic.isObtained = true;
                relic.currentX = START_X + (float) player.relics.size() * AbstractRelic.PAD_X;
                relic.currentY = START_Y;
                relic.targetX = relic.currentX;
                relic.targetY = relic.currentY;
                relic.flash();
                player.relics.add(relic);
                relic.hb.move(relic.currentX, relic.currentY);
                //relic.onEquip();
                relic.relicTip();
                UnlockTracker.markRelicAsSeen(relic.relicId);
            }

            if (AbstractDungeon.topPanel != null)
            {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }

            return true;
        }
    }

    private static class EnterUnnamedReign extends EYBEventPhase<TheMaskedTraveler2, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            GR.Common.Dungeon.EnterUnnamedReign();
            AddText("");
            AddLeaveOption();
            OpenMap();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public String Introduction()
        {
            return GetDescription(0);
        }

        public String Explanation(int goldBonus)
        {
            return GetDescription(1, goldBonus);
        }

        public String Offering()
        {
            return GetDescription(2);
        }

        public String ObtainRelicOption()
        {
            return GetOption(0);
        }

        public String ReplaceCardsOption(int remove, int add)
        {
            return GetOption(1, remove, add);
        }

        public String RecoverRelicsOption(int hpAmount)
        {
            return GetOption(2, hpAmount);
        }
    }
}