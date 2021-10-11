package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorWeakPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class KaijiItou_RestrictedRock extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaijiItou_RestrictedRock.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji);


    public KaijiItou_RestrictedRock()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Earth(2,0,0);
        SetAffinity_Air(1,0,0);

        SetExhaust(true);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                if (!intent.IsAttacking())
                {
                    return false;
                }
            }
        }

        return true;
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
        GameActions.Bottom.StackPower(new KaijiItou_RestrictedRockPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedRockPower extends AnimatorPower
    {
        private static final int MODIFIER = 25;

        public KaijiItou_RestrictedRockPower(AbstractCreature owner, int amount)
        {
            super(owner, KaijiItou_RestrictedRock.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            AnimatorWeakPower.AddEnemyModifier(MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            AnimatorWeakPower.AddEnemyModifier(-MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}