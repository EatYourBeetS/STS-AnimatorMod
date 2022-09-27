package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAffinitySealedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class Bozes extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Bozes.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Bozes()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2);
        SetAffinity_Light(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.Motivate(magicNumber);
        GameActions.Bottom.StackPower(new BozesPower(p, 1));
    }

    public class BozesPower extends AnimatorPower implements OnAffinitySealedSubscriber
    {
        public BozesPower(AbstractCreature owner, int amount)
        {
            super(owner, Bozes.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAffinitySealed.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAffinitySealed.Unsubscribe(this);
        }

        @Override
        public void OnAffinitySealed(EYBCard card, boolean manual)
        {
            if (player.hand.contains(card))
            {
                GameActions.Bottom.Draw(1);
                GameActions.Bottom.StackPower(new SupportDamagePower(owner, amount));
                flashWithoutSound();
            }
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            RemovePower();
        }
    }
}