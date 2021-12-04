package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CrowleyEusford extends AnimatorCard
{
    public static final EYBCardData DATA = Register(CrowleyEusford.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public CrowleyEusford()
    {
        super(DATA);

        Initialize(16, 0, 2);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.CanActivateSemiLimited(cardID)) {
            for (AbstractMonster mo: GameUtilities.GetEnemies(true)) {
                if (GameUtilities.GetPowerAmount(mo, VulnerablePower.POWER_ID) > 0) {
                    CombatStats.TryActivateSemiLimited(cardID);
                    GameActions.Bottom.StackPower(new VigorPower(player, magicNumber));
                    GameActions.Bottom.Flash(this);
                    break;
                }
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Motivate();
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return CombatStats.CardsExhaustedThisTurn().size() > 0;
    }
}