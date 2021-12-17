package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.LogHorizon.Soujiro;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Soujiro_Isami extends PCLCard
{
    public static final PCLCardData DATA = Register(Soujiro_Isami.class)
            .SetAttack(0, CardRarity.SPECIAL)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Isami()
    {
        super(DATA);

        Initialize(6, 0, 0);
        SetUpgrade(2, 0, 0);

        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (IsStarter())
        {
            PCLActions.Bottom.GainVelocity(1);
        }
    }
}