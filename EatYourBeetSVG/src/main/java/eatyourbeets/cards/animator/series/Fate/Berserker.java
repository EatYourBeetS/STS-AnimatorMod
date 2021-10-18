package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;

public class Berserker extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Berserker.class)
            .SetAttack(3, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Berserker()
    {
        super(DATA);

        Initialize(22, 0, 16);
        SetUpgrade(6, 0, 4);

        SetAffinity_Fire(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);

        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.ChangeStance(WrathStance.STANCE_ID);
                GameActions.Bottom.GainBlock(magicNumber);
            }
        });
    }
}