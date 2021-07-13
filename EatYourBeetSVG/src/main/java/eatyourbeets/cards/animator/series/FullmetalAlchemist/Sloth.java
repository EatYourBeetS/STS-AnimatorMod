package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.common.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class Sloth extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sloth.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Sloth()
    {
        super(DATA);

        Initialize(16, 13, 2, 9);
        SetUpgrade(0, 0, -1, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Dark(2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
        .SetDamageEffect(__ -> CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED,false));

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1, i > 0));
        }
    }
}