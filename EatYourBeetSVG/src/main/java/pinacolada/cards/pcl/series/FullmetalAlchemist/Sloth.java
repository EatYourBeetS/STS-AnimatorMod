package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.replacement.TemporaryDrawReductionPower;
import pinacolada.utilities.PCLActions;

public class Sloth extends PCLCard
{
    public static final PCLCardData DATA = Register(Sloth.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Brutal)
            .SetSeriesFromClassPackage();

    public Sloth()
    {
        super(DATA);

        Initialize(16, 13, 2, 9);
        SetUpgrade(2, 0, -1, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block).SetVFX(false, true);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY).forEach(d -> d
        .SetDamageEffect(__ ->
        {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED,false);
            return 0f;
        }));

        for (int i = 0; i < magicNumber; i++)
        {
            PCLActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1, i > 0));
        }
    }
}