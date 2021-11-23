package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.stances.MightStance;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class Lancer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lancer.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();
    public static final int VULNERABLE_MODIFIER = 25;

    public Lancer()
    {
        super(DATA);

        Initialize(6, 0, 1, VULNERABLE_MODIFIER);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(Affinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetVFXColor(Colors.Lerp(Color.SCARLET, Color.WHITE, 0.3f), Color.RED));

        if (MightStance.IsActive()) {
            GameActions.Bottom.GainVelocity(magicNumber, true);
        }
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (TrySpendAffinity(Affinity.Red))
        {
            GameActions.Bottom.StackPower(new LancerPower(p, 2));
        }
    }

    public static class LancerPower extends AnimatorPower
    {
        public LancerPower(AbstractCreature owner, int amount)
        {
            super(owner, Lancer.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, Lancer.VULNERABLE_MODIFIER);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            AnimatorVulnerablePower.AddEnemyModifier(Lancer.VULNERABLE_MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            AnimatorVulnerablePower.AddEnemyModifier(-Lancer.VULNERABLE_MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}