package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Berserker extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Berserker.class)
            .SetAttack(3, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Berserker()
    {
        super(DATA);

        Initialize(19, 0, 3, 12);
        SetUpgrade(5, 0, 0, 2);

        SetAffinity_Red(2, 0, 6);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d
        .AddCallback(m.currentBlock, (initialBlock, target) ->
        {
            if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0))
            {
                GameActions.Bottom.GainBlock(this.secondaryValue);
            }
        }));
        GameActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);
        GameActions.Bottom.GainForce(magicNumber);

        if (TrySpendAffinity(Affinity.Red))
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}