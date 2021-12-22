package pinacolada.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.utilities.JUtils;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class AfterLifeMod extends AbstractCardModifier
{
    public static final String ID = GR.PCL.CreateID("Afterlife");
    public static final String LABEL = GR.PCL.Strings.CardMods.AfterLifeLocked;

    public static void Add(AbstractCard card)
    {
        JUtils.LogError(card, "TESTING ADD");
        if (PCLGameUtilities.InGame() && PCLGameUtilities.InBattle()) {
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
                .SetUseCondition(control -> control.card.hasTag(AFTERLIFE) && player.exhaustPile.contains(control.card))
                .OnSelect(control ->
                    {
                        if (control.card.canUse(player, null))
                        {
                            if (PCLJUtils.Find(player.exhaustPile.group, AfterLifeMod::CanPurge) == null)
                            {
                                PCLGameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, LABEL, true));
                                return;
                            }

                            PCLActions.Bottom.SelectCreature(control.card).AddCallback(control, (state, creature) ->
                            {
                                //Put this here so the cost is only paid upon successful completion of the selectCreature action
                                AbstractCard cardToPurge = PCLJUtils.Random(PCLJUtils.Filter(player.exhaustPile.group, AfterLifeMod::CanPurge));
                                if (cardToPurge == null) {
                                    return;
                                }
                                AbstractCard copy = cardToPurge.makeStatEquivalentCopy();
                                PCLGameEffects.List.ShowCardBriefly(copy);
                                PCLGameEffects.List.Add(new ExhaustCardEffect(copy));

                                PCLActions.Bottom.Purge(cardToPurge).SetDuration(0.1f, true);
                                PCLActions.Bottom.PlayCard(state.card, player.exhaustPile, PCLJUtils.SafeCast(creature, AbstractMonster.class))
                                        .SetPurge(true)
                                        .SpendEnergy(true).AddCallback(() -> {
                                            PCLCombatStats.OnPurge(state.card, player.exhaustPile);
                                            PCLCombatStats.OnAfterlife(state.card, copy);
                                        });
                            });
                        }
                    });
    }

    private static boolean CanPurge(AbstractCard card)
    {
        return !AfterLifeMod.IsAdded(card);
    }
}

