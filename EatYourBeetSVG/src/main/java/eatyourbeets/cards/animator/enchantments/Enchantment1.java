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
    public static final int UP1_GAIN_BLOCK = 4;
    public static final int UP3_TAKE_DAMAGE = 3;

    private static final CardEffectChoice choices = new CardEffectChoice();
    private static final float D_X = CardGroup.DRAW_PILE_X * 1.5f;
    private static final float D_Y = CardGroup.DRAW_PILE_Y * 3.5f;

    public Enchantment1()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 0, 2);
    }

    @Override
    protected String GetRawDescription()
    {
        return upgradeIndex == 1 ? super.GetRawDescription(UP1_GAIN_BLOCK)
             : upgradeIndex == 3 ? super.GetRawDescription(UP3_TAKE_DAMAGE)
             : super.GetRawDescription();
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
            GameActions.Bottom.Add(new EndPlayerTurn());
            return;
        }

        GameActions.Bottom.Add(new RandomCostReduction(1, false));

        switch (upgradeIndex)
        {
            case 1: GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID); return;
            case 2: GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID); return;
            case 3: GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID); return;
            default: throw new RuntimeException("Invalid upgrade index: " + upgradeIndex);
        }
    }
}