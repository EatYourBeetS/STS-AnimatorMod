package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class Lancer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lancer.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Lancer()
    {
        super(DATA);

        Initialize(6, 0, 1, 25);
        SetUpgrade(0, 0, 0, 25);

        SetAffinity_Air();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Colors.Lerp(Color.SCARLET, Color.WHITE, 0.3f), Color.RED);

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        GameActions.Bottom.StackPower(new LancerPower(p, 2, secondaryValue));
    }

    public static class LancerPower extends AnimatorPower
    {
        int vulnerableAmount;

        public LancerPower(AbstractCreature owner, int amount, int vulnerableAmount)
        {
            super(owner, Lancer.DATA);

            this.vulnerableAmount = vulnerableAmount;

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            AnimatorVulnerablePower.AddEnemyModifier(vulnerableAmount);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            AnimatorVulnerablePower.AddEnemyModifier(vulnerableAmount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}