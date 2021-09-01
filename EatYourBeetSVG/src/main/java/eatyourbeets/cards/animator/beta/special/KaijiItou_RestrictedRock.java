package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
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
        SetAffinity_Orange(2,0,0);
        SetAffinity_Green(1,0,0);

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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
        GameActions.Bottom.StackPower(new KaijiItou_RestrictedRockPower(p, secondaryValue));
    }

    public static class KaijiItou_RestrictedRockPower extends AnimatorPower
    {
        private static final float MODIFIER = 1f;

        public KaijiItou_RestrictedRockPower(AbstractCreature owner, int amount)
        {
            super(owner, KaijiItou_RestrictedRock.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.EnemyWeakModifier += MODIFIER;
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.EnemyWeakModifier -= MODIFIER;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}