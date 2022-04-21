package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Sloth extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sloth.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Sloth()
    {
        super(DATA);

        Initialize(14, 12, 2, 9);
        SetUpgrade(0, 0, -1, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block).SetVFX(false, true);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY)
        .SetDamageEffect(__ ->
        {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED,false);
            return 0f;
        });

        GameActions.Bottom.DrawReduction(magicNumber);
    }
}