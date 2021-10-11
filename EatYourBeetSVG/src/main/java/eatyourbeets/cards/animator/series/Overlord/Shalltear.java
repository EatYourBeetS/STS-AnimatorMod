package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shalltear extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shalltear.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(CardSeries.Overlord);

    public Shalltear()
    {
        super(DATA);

        Initialize(2, 0, 2, 6);
        SetUpgrade(1, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(2, 0, 3);
        SetAffinity_Dark(2, 0, 3);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE)
        .SetDamageEffect((enemy, __) -> GameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)))
        .AddCallback(enemies ->
        {
            int healAmount = 0;
            for (AbstractCreature enemy : enemies)
            {
                final int loseHP = Math.min(magicNumber, enemy.currentHealth);
                if (loseHP > 0)
                {
                    GameActions.Bottom.LoseHP(player, enemy, loseHP, AttackEffects.NONE);
                    healAmount += loseHP;
                }
            }

            if (healAmount > 0)
            {
                GameActions.Bottom.HealPlayerLimited(this, healAmount);
            }
        });

        final AbstractAffinityPower power = CombatStats.Affinities.GetPower(Affinity.Light);
        if (power.amount > 0)
        {
            final int amount = Math.min(secondaryValue, power.amount);
            GameActions.Bottom.RaiseDarkLevel(amount);
            power.reducePower(amount);
        }
        else {
            GameActions.Bottom.ChangeStance(CorruptionStance.STANCE_ID);
        }
    }
}