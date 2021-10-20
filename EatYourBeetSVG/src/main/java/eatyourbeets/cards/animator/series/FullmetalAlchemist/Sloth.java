package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.DeenergizedPower;
import eatyourbeets.utilities.GameActions;

public class Sloth extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sloth.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Sloth()
    {
        super(DATA);

        Initialize(14, 12, 1, 2);
        SetUpgrade(4, 2, 0, 0);

        SetAffinity_Earth(2);
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

        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, magicNumber));

        GameActions.Bottom.StackPower(new DeenergizedPower(p, secondaryValue));
    }
}