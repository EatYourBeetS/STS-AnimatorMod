package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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

        SetAffinity_Fire(2);
        SetAffinity_Air(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Colors.Lerp(Color.SCARLET, Color.WHITE, 0.3f), Color.RED);

        if (ForceStance.IsActive()) {
            GameUtilities.MaintainPower(Affinity.Air);
        }
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (info.TryActivateSemiLimited())
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