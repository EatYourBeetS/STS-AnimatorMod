package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Lancer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lancer.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Lancer()
    {
        super(DATA);

        Initialize(6, 0, 1, 25);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(2);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Colors.Lerp(Color.SCARLET, Color.WHITE, 0.3f));

        GameUtilities.RetainPower(AffinityType.Green);
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ApplyPower(new LancerPower(p, secondaryValue));
        }
    }

    public static class LancerPower extends AnimatorPower
    {
        private final float modifier;

        public LancerPower(AbstractCreature owner, int amount)
        {
            super(owner, Lancer.DATA);

            this.modifier = amount / 100f;
            this.hideAmount = true;

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.EnemyVulnerableModifier += modifier;
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.EnemyVulnerableModifier -= modifier;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}