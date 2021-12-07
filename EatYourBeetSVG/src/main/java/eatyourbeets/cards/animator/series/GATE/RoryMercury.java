package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class RoryMercury extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoryMercury.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public RoryMercury()
    {
        super(DATA);

        Initialize(8, 0, 5);
        SetUpgrade(2, 0, 6);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Dark(1, 0, 2);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Green(0,0,1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (GameUtilities.GetPower(enemy, AnimatorVulnerablePower.class) != null) {
            amount += magicNumber;
        }
        if (GameUtilities.GetPower(player, AnimatorVulnerablePower.class) != null) {
            amount += magicNumber;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY).forEach(d -> d.AddCallback((t) ->
        {
            if (GameUtilities.IsDeadOrEscaped(t) && CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.ApplyVulnerable(TargetHelper.Player(), 1);
                GameActions.Bottom.GainTemporaryHP(t.lastDamageTaken);
                GameActions.Bottom.Exhaust(this);
            }
        }));

    }
}