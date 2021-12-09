package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.replacement.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class Sloth extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sloth.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Brutal)
            .SetSeriesFromClassPackage();

    public Sloth()
    {
        super(DATA);

        Initialize(16, 13, 2, 9);
        SetUpgrade(2, 0, -1, 0);

        SetAffinity_Red(1, 0, 4);
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
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY).forEach(d -> d
        .SetDamageEffect(__ ->
        {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED,false);
            return 0f;
        }));

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1, i > 0));
        }
    }
}