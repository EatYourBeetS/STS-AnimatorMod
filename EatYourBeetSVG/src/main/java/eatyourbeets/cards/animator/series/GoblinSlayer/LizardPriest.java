package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class LizardPriest extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int TEMPORARY_THORNS = 2;
    public static final int BLOCK_NEXT_TURN = 3;


    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 4, BLOCK_NEXT_TURN, TEMPORARY_THORNS);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new LizardPriestPower(p, 1));
    }

    public static class LizardPriestPower extends AnimatorPower
    {
        public LizardPriestPower(AbstractCreature owner, int amount)
        {
            super(owner, LizardPriest.DATA);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.SelectFromHand(name, amount, false)
            .SetOptions(true, true, true)
            .SetMessage(RetainCardsAction.TEXT[0])
            .SetFilter(c -> GameUtilities.CanRetain(c) && c.type == CardType.ATTACK)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameUtilities.Retain(c);
                    EYBCard ec = JUtils.SafeCast(c, EYBCard.class);
                    if (ec != null) {
                        switch (ec.attackType) {
                            case Elemental:
                                GameActions.Bottom.GainInspiration(1);
                                break;
                            case Piercing:
                                GameActions.Bottom.GainTemporaryThorns(TEMPORARY_THORNS);
                                break;
                            default:
                                GameActions.Bottom.StackPower(new NextTurnBlockPower(player, BLOCK_NEXT_TURN));
                                break;
                        }
                    }
                    else {
                        GameActions.Bottom.StackPower(new NextTurnBlockPower(player, BLOCK_NEXT_TURN));
                    }
                }
            });
            RemovePower();
        }
    }
}