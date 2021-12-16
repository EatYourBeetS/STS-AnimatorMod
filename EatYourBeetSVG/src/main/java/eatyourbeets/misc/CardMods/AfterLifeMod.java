package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static eatyourbeets.resources.GR.Enums.CardTags.AFTERLIFE;

public class AfterLifeMod extends AbstractCardModifier
{
    public static final String ID = GR.Animator.CreateID("Afterlife");
    public static final String LABEL = GR.Animator.Strings.CardMods.AfterLifeLocked;

    public static void Add(AbstractCard card)
    {
        CardModifierManager.addModifier(card, new AfterLifeMod());
        if (GameUtilities.InGame() && GameUtilities.InBattle()) {
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
        CombatStats.ControlPile.Add(card)
                .SetUseCondition(control -> control.card.hasTag(AFTERLIFE) && player.exhaustPile.contains(control.card))
                .OnSelect(control ->
                    {
                        if (control.card.canUse(player, null))
                        {
                            if (JUtils.Find(player.exhaustPile.group, AfterLifeMod::CanPurge) == null)
                            {
                                GameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, LABEL, true));
                                return;
                            }

                            GameActions.Bottom.SelectCreature(control.card).AddCallback(control, (state, creature) ->
                            {
                                //Put this here so the cost is only paid upon successful completion of the selectCreature action
                                AbstractCard cardToPurge = JUtils.Random(JUtils.Filter(player.exhaustPile.group, AfterLifeMod::CanPurge));
                                if (cardToPurge == null) {
                                    return;
                                }
                                AbstractCard copy = cardToPurge.makeStatEquivalentCopy();
                                GameEffects.List.ShowCardBriefly(copy);
                                GameEffects.List.Add(new ExhaustCardEffect(copy));

                                GameActions.Bottom.Purge(cardToPurge).SetDuration(0.1f, true);
                                GameActions.Bottom.PlayCard(state.card, player.exhaustPile, JUtils.SafeCast(creature, AbstractMonster.class))
                                        .SetPurge(true)
                                        .SpendEnergy(true).AddCallback(() -> {
                                            CombatStats.OnPurge(state.card, player.exhaustPile);
                                            CombatStats.OnAfterlife(state.card, copy);
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

