package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.replacement.PCLVulnerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class RoryMercury extends PCLCard
{
    public static final PCLCardData DATA = Register(RoryMercury.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public RoryMercury()
    {
        super(DATA);

        Initialize(8, 0, 5);
        SetUpgrade(2, 0, 6);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Dark(1, 0, 2);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Green(0,0,1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (PCLGameUtilities.GetPower(enemy, PCLVulnerablePower.class) != null) {
            amount += magicNumber;
        }
        if (PCLGameUtilities.GetPower(player, PCLVulnerablePower.class) != null) {
            amount += magicNumber;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY).forEach(d -> d.AddCallback((t) ->
        {
            if (PCLGameUtilities.IsDeadOrEscaped(t) && CombatStats.TryActivateLimited(cardID))
            {
                PCLActions.Bottom.ApplyVulnerable(TargetHelper.Player(), 1);
                PCLActions.Bottom.GainTemporaryHP(t.lastDamageTaken);
                PCLActions.Bottom.Exhaust(this);
            }
        }));

    }
}