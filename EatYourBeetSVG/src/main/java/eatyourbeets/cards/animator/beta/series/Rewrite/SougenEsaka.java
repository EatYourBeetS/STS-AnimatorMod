package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.WillpowerPower;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(1, 0, 1);
        SetAffinity_Orange(1, 1, 1);

        SetAffinityRequirement(AffinityType.Orange, 3);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        int willpower = GameUtilities.GetPowerAmount(AbstractDungeon.player, WillpowerPower.POWER_ID);
        return super.ModifyBlock(enemy, amount + willpower);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT);

        int willpower = GameUtilities.GetPowerAmount(p, WillpowerPower.POWER_ID);
        if (willpower > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        if (CheckAffinity(AffinityType.Orange) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChangeStance(WillpowerStance.STANCE_ID);
            GameActions.Bottom.GainWillpower(magicNumber);
        }
    }
}