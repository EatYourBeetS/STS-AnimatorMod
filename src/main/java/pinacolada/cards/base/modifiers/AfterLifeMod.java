package pinacolada.cards.base.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardAffinity;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class AfterLifeMod extends AbstractCardModifier
{
    public static final String ID = GR.PCL.CreateID("Afterlife");
    protected static AbstractCard currentCard;

    public static void Add(AbstractCard card)
    {
        if (PCLGameUtilities.InGame() && PCLGameUtilities.InBattle() && !IsAdded(card)) {
            CardModifierManager.addModifier(card, new AfterLifeMod());
            AfterlifeAddToControlPile(card);
        }
    }

    public static boolean IsAdded(AbstractCard card)
    {
        return CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public AbstractCardModifier makeCopy()
    {
        return new AfterLifeMod();
    }

    @Override
    public String identifier(AbstractCard card)
    {
        return ID;
    }

    public static void AfterlifeAddToControlPile(AbstractCard card)
    {
        PCLCombatStats.ControlPile.Add(card)
                .SetUseCondition(control -> CanUse(control.card))
                .OnSelect(control ->
                    {
                        if (CanUse(control.card))
                        {
                            currentCard = control.card;
                            PCLCardAffinities pAffinities = PCLGameUtilities.GetPCLAffinities(control.card);
                            PCLActions.Bottom.SelectCreature(control.card).AddCallback(control, (state, creature) ->
                            {
                                PCLActions.Bottom.PurgeFromPile(control.card.name, 9999, player.exhaustPile, player.hand)
                                        .SetOptions(false, true)
                                        .SetFilter(c -> CanPurge(control.card, c, pAffinities))
                                        .SetCompletionRequirement(AfterLifeMod::ConditionMet)
                                        .SetDynamicMessage(AfterLifeMod::GetDynamicLabel)
                                        .AddCallback((c) -> {
                                            currentCard = null;
                                            if (c.size() > 0) {
                                                PCLActions.Bottom.PlayCard(state.card, player.exhaustPile, PCLJUtils.SafeCast(creature, AbstractMonster.class))
                                                        .SetPurge(true)
                                                        .SpendEnergy(true).AddCallback(() -> {
                                                            PCLCombatStats.OnPurge(state.card, player.exhaustPile);
                                                            PCLCombatStats.OnAfterlife(state.card, c);
                                                        });
                                            }
                                        });

                            });
                        }
                    });
    }

    private static boolean CanPurge(AbstractCard controlCard, AbstractCard card, PCLCardAffinities pAffinities)
    {
        return !controlCard.cardID.equals(card.cardID)
                && (pAffinities == null || (pAffinities.HasStar() && PCLGameUtilities.GetPCLAffinityLevel(card, PCLAffinity.General, true) > 0)
                || PCLJUtils.Any(pAffinities.List, af -> af.level > 0 && PCLGameUtilities.GetPCLAffinityLevel(card, af.type, true) > 0));
    }

    private static boolean CanUse(AbstractCard card)
    {
        if (!(card.hasTag(AFTERLIFE) && player.exhaustPile.contains(card) && card.canUse(player, null))) {
            return false;
        }
        return ConditionMet(card, player.exhaustPile.group, player.hand.group);
    }

    protected static boolean ConditionMet(ArrayList<AbstractCard> cards) {
        return ConditionMet(currentCard, cards);
    }

    @SafeVarargs
    protected static boolean ConditionMet(AbstractCard card, ArrayList<AbstractCard>... cardLists) {
        if (card != null) {
            PCLCardAffinities pAffinities = PCLGameUtilities.GetPCLAffinities(card);
            if (pAffinities != null) {
                return BooleanArrayMet(GetRequiredAffinities(card, cardLists));
            }
        }
        return false;
    }

    protected static String GetDynamicLabel(ArrayList<AbstractCard> cards) {
        final boolean[] required = GetRequiredAffinities(currentCard, cards);

        return BooleanArrayMet(required) ? GR.PCL.Strings.CardMods.AfterlifeMet : PCLJUtils.Format(GR.PCL.Strings.CardMods.AfterlifeRequirement,
                PCLJUtils.JoinStrings(", ",
                PCLJUtils.Map(
                        PCLJUtils.Filter(PCLAffinity.Extended(), af -> required[af.ID])
                        , PCLAffinity::GetTooltip)));
    }

    @SafeVarargs
    protected static boolean[] GetRequiredAffinities(AbstractCard card, ArrayList<AbstractCard>... cardLists) {
        final boolean[] requiredAffinities = new boolean[7];
        PCLCardAffinities pAffinities = PCLGameUtilities.GetPCLAffinities(card);
        if (pAffinities == null) {
            return requiredAffinities;
        }
        if (pAffinities.HasStar()) {
            for (PCLAffinity af : PCLAffinity.Basic()) {
                requiredAffinities[af.ID] = true;
            }
        }
        else {
            for (PCLCardAffinity cf : pAffinities.List) {
                requiredAffinities[cf.type.ID] = cf.level > 0;
            }
        }

        for (ArrayList<AbstractCard> list : cardLists) {
            for (AbstractCard c2 : list) {
                PCLCardAffinities pAffinities2 = PCLGameUtilities.GetPCLAffinities(c2);
                if (!c2.cardID.equals(card.cardID) && pAffinities2 != null) {
                    if (pAffinities2.HasStar()) {
                        Arrays.fill(requiredAffinities, false);
                    }
                    else {
                        for (PCLCardAffinity cf : PCLGameUtilities.GetPCLAffinities(c2).List) {
                            requiredAffinities[cf.type.ID] = requiredAffinities[cf.type.ID] & cf.level == 0;
                        }
                    }
                }
            }
        }

        return requiredAffinities;
    }

    protected static boolean BooleanArrayMet(boolean[] values) {
        for (boolean b : values) {
            if (b) {
                return false;
            }
        }
        return true;
    }
}

