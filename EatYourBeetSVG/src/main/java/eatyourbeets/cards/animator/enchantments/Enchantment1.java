package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostReduction;
import eatyourbeets.actions.player.EndPlayerTurn;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.effects.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.PowerTriggerCondition;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Enchantment1 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment1.class);
    public static final int INDEX = 1;

    private static final CardEffectChoice choices = new CardEffectChoice();
    private static final float D_X = CardGroup.DRAW_PILE_X * 1.5f;
    private static final float D_Y = CardGroup.DRAW_PILE_Y * 3.5f;

    public Enchantment1()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 0, 2);
        SetUpgrade(0, 0, 0, -1);
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 3;
    }

    @Override
    public void SetUses(PowerTriggerCondition triggerCondition)
    {
        triggerCondition.SetUses(1, false, true);
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
            }

            choices.Select(1, null);
        }
        else {
            switch (upgradeIndex)
            {
                case 1: GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID); break;
                case 2: GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID); break;
                case 3: GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID); break;
                default: throw new RuntimeException("Invalid upgrade index: " + upgradeIndex);
            }
        }

        GameActions.Bottom.Add(new EndPlayerTurn());
    }
}