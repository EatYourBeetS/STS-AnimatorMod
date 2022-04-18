package eatyourbeets.ui.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.basic.*;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.Loadout_Konosuba;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class EYBCardPopupActions
{
    public static final ArrayList<AnimatorCard> PermanentActions = new ArrayList<>();

    public static class FullmetalAlchemist_Alphonse extends EYBCardPopupAction
    {
        protected final int MAX_HP_LOSS;
        protected final EYBCardData REQUIRED1;
        protected final EYBCardData TARGET1;

        public FullmetalAlchemist_Alphonse(int maxHPLoss, EYBCardData requiredCard, EYBCardData targetCard)
        {
            MAX_HP_LOSS = maxHPLoss;
            REQUIRED1 = requiredCard;
            TARGET1 = targetCard;

            SetText(specialActions.TransformAndLoseMaxHP_T(MAX_HP_LOSS), terms.Transform, specialActions.RequireCardAndTransform_D(requiredCard.Strings.NAME, TARGET1.Strings.NAME));
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsRestRoom() && !HasCard(TARGET1) && HasHp(MAX_HP_LOSS) && HasCard(card) && HasCard(REQUIRED1);
        }

        @Override
        public void Execute()
        {
            if (Replace(card, TARGET1, card.upgraded) != null)
            {
                LoseMaxHP(MAX_HP_LOSS);
                SFX.Play(SFX.ATTACK_MAGIC_FAST_3, 0.4f);
                Complete();
            }
        }
    }

    public static class HitsugiNoChaika_Tooru extends EYBCardPopupAction
    {
        protected final int HP_LOSS;
        protected final EYBCardData REQUIRED1;
        protected final EYBCardData TARGET1;

        public HitsugiNoChaika_Tooru(int hpLoss, EYBCardData requiredCard, EYBCardData targetCard)
        {
            HP_LOSS = hpLoss;
            REQUIRED1 = requiredCard;
            TARGET1 = targetCard;

            SetText(specialActions.TransformAndLoseHP_T(HP_LOSS), terms.Transform, specialActions.RequireCardAndTransform_D(requiredCard.Strings.NAME, TARGET1.Strings.NAME));
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsRestRoom() && !HasCard(TARGET1) && HasHp(HP_LOSS) && HasCard(card) && HasCard(REQUIRED1);
        }

        @Override
        public void Execute()
        {
            if (Replace(card, TARGET1, card.upgraded) != null)
            {
                LoseHP(HP_LOSS);
                SFX.Play(SFX.EVENT_VAMP_BITE, 0.4f);
                Complete();
            }
        }
    }

    public static class Konosuba_Sylvia extends EYBCardPopupAction
    {
        protected final int HP_LOSS;
        protected final EYBCardData REQUIRED1;
        protected final EYBCardData REQUIRED2;
        protected final EYBCardData TARGET1;

        public Konosuba_Sylvia(int hpLoss, EYBCardData requiredCard1, EYBCardData requiredCard2, EYBCardData targetCard)
        {
            HP_LOSS = hpLoss;
            REQUIRED1 = requiredCard1;
            REQUIRED2 = requiredCard2;
            TARGET1 = targetCard;

            SetText(specialActions.TransformAndLoseHP_T(HP_LOSS), terms.Transform, specialActions.RemoveAndTransform_D(REQUIRED1.Strings.NAME, REQUIRED2.Strings.NAME, TARGET1.Strings.NAME));
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsRestRoom() && !HasCard(TARGET1) && HasHp(HP_LOSS) && HasCard(card) && HasCard(REQUIRED1) && HasCard(REQUIRED2);
        }

        @Override
        public void Execute()
        {
            final EYBCard r1 = Find(REQUIRED1, true);
            final EYBCard r2 = Find(REQUIRED2, true);
            if (r1 == null || r2 == null)
            {
                JUtils.LogError(Loadout_Konosuba.class, "Couldn't find required cards in masterdeck.");
                return;
            }

            if (Replace(card, TARGET1, card.upgraded) != null)
            {
                LoseHP(HP_LOSS);
                Remove(r1);
                Remove(r2);
                SFX.Play(SFX.SLIME_SPLIT, 0.85f);
                Complete();
            }
        }
    }

    public static class MadokaMagica_Witch extends EYBCardPopupAction
    {
        protected final EYBCardData TARGET1;
        protected final EYBCardData TARGET2;

        public MadokaMagica_Witch(EYBCardData targetCard)
        {
            TARGET1 = targetCard;
            TARGET2 = Curse_GriefSeed.DATA;

            SetText(specialActions.TransformAndObtainGriefSeed_T(), terms.Transform, specialActions.TransformGeneric_D(TARGET1.Strings.NAME));
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsRestRoom() && !HasCard(TARGET1) && HasCard(card);
        }

        @Override
        public void Execute()
        {
            if (Replace(card, TARGET1, card.upgraded) != null)
            {
                Obtain(TARGET2.MakeCopy(false));
                SFX.Play(SFX.TINGSHA, 0.4f);
                Complete();
            }
        }
    }

    public static class TouhouProject_Remilia extends EYBCardPopupAction
    {
        protected final EYBCardData TARGET1;
        protected final int VULNERABLE_AMOUNT;

        public TouhouProject_Remilia(EYBCardData targetCard, int vulnerableAmount)
        {
            TARGET1 = targetCard;
            VULNERABLE_AMOUNT = vulnerableAmount;

            SetText(specialActions.ObtainCardAndGainVulnerable(VULNERABLE_AMOUNT), terms.Obtain, specialActions.ObtainCardAndGainVulnerable_D(TARGET1.Strings.NAME, VULNERABLE_AMOUNT));
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsRestRoom() && !HasCard(TARGET1) && HasCard(card);
        }

        @Override
        public void Execute()
        {
            final EYBCard card = TARGET1.MakeCopy(false);
            Obtain(card);
            SFX.Play(SFX.ORB_LIGHTNING_EVOKE, 0.4f);
            card.misc = VULNERABLE_AMOUNT;
            Complete();
        }
    }

    public static class ImproveBasicCard extends EYBCardPopupAction
    {
        private static final String LAST_IMPROVEMENT_PRICE = ImproveBasicCard.class.getSimpleName() + ".LastImprovementPrice";

        protected final int BASE_GOLD_COST;
        protected final int GOLD_STEP;
        protected int GOLD_COST;

        public ImproveBasicCard()
        {
            GOLD_COST = BASE_GOLD_COST = ImprovedBasicCard.IMPROVEMENT_COST;
            GOLD_STEP = ImprovedBasicCard.IMPROVEMENT_COST_STEP;

            SetText(specialActions.Improve_T(GOLD_COST), terms.Transform, specialActions.Improve_D());
        }

        @Override
        public void Initialize(EYBCard card)
        {
            super.Initialize(card);

            GOLD_COST = GR.Common.Dungeon.GetInteger(LAST_IMPROVEMENT_PRICE, BASE_GOLD_COST);
            name = specialActions.Improve_T(GOLD_COST);
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsRestRoom() && HasCard(card) && HasGold(GOLD_COST);
        }

        @Override
        public void Execute()
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final ArrayList<EYBCardData> cardData = card.type == AbstractCard.CardType.ATTACK ? ImprovedStrike.GetCards() : ImprovedDefend.GetCards();
            for (EYBCardData data : cardData)
            {
                if ((data != Defend_Star.DATA) && (data != Strike_Star.DATA))
                {
                    group.addToBottom(data.MakeCopy(card.upgraded));
                }
            }

            Complete();
            GameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
            .HideTopPanel(true)
            .CancellableFromPlayer(true)
            .AddCallback(card, (c, cards) ->
            {
                if (cards != null && cards.size() > 0 && Replace(c, (EYBCard) cards.get(0)) != null)
                {
                    LoseGold(GOLD_COST);
                    SFX.Play(SFX.ATTACK_IRON_2, 0.66f, 0.66f, 0.825f);
                    GR.Common.Dungeon.SetData(LAST_IMPROVEMENT_PRICE, GOLD_COST + GOLD_STEP);
                }
            }))
            .ShowBlackScreen(0.95f);
        }
    }

    public static class HealAndObtainCurse extends EYBCardPopupAction
    {
        protected final EYBCardData TARGET1;
        protected final int HEAL_AMOUNT;
        protected final int REQUIRED_CURSES;

        public HealAndObtainCurse(EYBCardData targetCard, int requiredCurses, int heal)
        {
            TARGET1 = targetCard;
            HEAL_AMOUNT = heal;
            REQUIRED_CURSES = requiredCurses;

            SetText(specialActions.ObtainCurseAndHeal_T(HEAL_AMOUNT), terms.Transform, specialActions.ObtainCurseAndHeal_D(TARGET1.Strings.NAME, REQUIRED_CURSES));
        }

        @Override
        public boolean CanExecute(AbstractCard card)
        {
            return IsAnimator() && IsRestRoom() && !HasCard(TARGET1) && Count(c -> c.type == AbstractCard.CardType.CURSE) >= REQUIRED_CURSES;
        }

        @Override
        public void Execute()
        {
            Obtain(TARGET1.MakeCopy(false));
            SFX.Play(SFX.ORB_DARK_CHANNEL, 0.4f);
            Heal(HEAL_AMOUNT);
            Complete();
        }
    }
}
