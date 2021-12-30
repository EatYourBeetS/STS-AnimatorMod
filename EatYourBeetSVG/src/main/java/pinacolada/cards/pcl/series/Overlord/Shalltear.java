package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.stances.DesecrationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Shalltear extends PCLCard
{
    public static final PCLCardData DATA = Register(Shalltear.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Dark, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeries(CardSeries.Overlord);

    public Shalltear()
    {
        super(DATA);

        Initialize(2, 0, 2, 5);
        SetUpgrade(1, 0, 1);

        SetAffinity_Blue(1, 0, 3);
        SetAffinity_Dark(1, 0, 3);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect((enemy, __) -> PCLGameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)))
        .AddCallback(enemies ->
        {
            int healAmount = 0;
            for (AbstractCreature enemy : enemies)
            {
                final int loseHP = Math.min(magicNumber, enemy.currentHealth);
                if (loseHP > 0)
                {
                    PCLActions.Bottom.LoseHP(player, enemy, loseHP, AttackEffects.NONE);
                    healAmount += loseHP;
                }
            }

            if (healAmount > 0)
            {
                PCLActions.Bottom.HealPlayerLimited(this, healAmount);
            }
        }));

        final AbstractPCLAffinityPower powerLight = PCLCombatStats.MatchingSystem.GetPower(PCLAffinity.Light);
        final AbstractPCLAffinityPower powerDark = PCLCombatStats.MatchingSystem.GetPower(PCLAffinity.Dark);
        final int lightAmount = Math.min(secondaryValue, powerLight.amount);
        PCLActions.Bottom.GainDesecration(lightAmount);
        powerLight.reducePower(lightAmount);
        if (powerDark.amount + lightAmount >= powerDark.GetEffectiveThreshold())
        {
            PCLActions.Bottom.ChangeStance(DesecrationStance.STANCE_ID);
        }
    }
}