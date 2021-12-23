package pinacolada.cards.pcl.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class Lancer extends PCLCard
{
    public static final PCLCardData DATA = Register(Lancer.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage();
    public static final int VULNERABLE_MODIFIER = 25;

    public Lancer()
    {
        super(DATA);

        Initialize(6, 0, 1, VULNERABLE_MODIFIER);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetVFXColor(Colors.Lerp(Color.SCARLET, Color.WHITE, 0.3f), Color.RED));

        PCLActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.StackPower(new LancerPower(p, 2));
        }
    }

    public static class LancerPower extends PCLPower
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

            PCLCombatStats.AddEffectBonus(VulnerablePower.POWER_ID, Lancer.VULNERABLE_MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.AddEffectBonus(VulnerablePower.POWER_ID, -Lancer.VULNERABLE_MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}