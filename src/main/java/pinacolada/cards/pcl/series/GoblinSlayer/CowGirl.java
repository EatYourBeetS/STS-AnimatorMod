package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;

public class CowGirl extends PCLCard
{
    public static final PCLCardData DATA = Register(CowGirl.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None, true)
            .SetSeriesFromClassPackage();

    public CowGirl()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0,0,2);

        SetAffinity_Orange(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.Motivate();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        (upgraded ? PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile, player.discardPile)
                  : PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile))
        .SetOptions(false, false)
        .SetFilter(c -> c.type == CardType.ATTACK && c instanceof PCLCard && ((PCLCard) c).attackType == PCLAttackType.Normal).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Orange, secondaryValue);
            }
        });
    }
}