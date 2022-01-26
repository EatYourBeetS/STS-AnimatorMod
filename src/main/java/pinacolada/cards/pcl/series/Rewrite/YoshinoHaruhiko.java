package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;

public class YoshinoHaruhiko extends PCLCard
{
    public static final PCLCardData DATA = Register(YoshinoHaruhiko.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.Random).SetSeriesFromClassPackage();

    public YoshinoHaruhiko()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(2, 0, 0);
        SetExhaust(true);
        SetAffinity_Red(1, 0, 1);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);

        if (IsStarter())
        {
            PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Red, 1);
                        }
                    });
        }
    }
}