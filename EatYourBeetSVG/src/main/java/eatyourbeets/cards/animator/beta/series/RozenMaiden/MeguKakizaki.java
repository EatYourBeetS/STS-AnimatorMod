package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MeguKakizaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MeguKakizaki.class)
    		.SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    
    public MeguKakizaki()
    {
        super(DATA);

        Initialize(0, 5, 5, 3);
        SetUpgrade(0, 1, 5, 0);
        SetAffinity_Light(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetAffinityRequirement(Affinity.Dark, 8);

        SetEthereal(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        heal = GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID) > 0 ? block : 0;
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Callback(() ->
        {
            if (CheckAffinity(Affinity.Dark)) {
                GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), secondaryValue);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block).AddCallback(() -> {
            int toTransfer = Math.min(magicNumber, GameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID));
            GameActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, toTransfer);
            AbstractMonster mo = GameUtilities.GetRandomEnemy(true);
            if (mo != null && toTransfer > 0) {
                GameActions.Bottom.DealDamageAtEndOfTurn(player, mo, toTransfer, AttackEffects.CLAW);
            }
            else if (toTransfer == 0) {
                GameActions.Bottom.RecoverHP(heal);
                GameActions.Bottom.Exhaust(this);
            }
        });
    }
}

