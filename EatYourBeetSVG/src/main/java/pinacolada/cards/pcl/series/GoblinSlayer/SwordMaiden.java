package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ListSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;

public class SwordMaiden extends PCLCard
{
    public static final PCLCardData DATA = Register(SwordMaiden.class)
            .SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetSeriesFromClassPackage();

    public SwordMaiden()
    {
        super(DATA);

        Initialize(0, 0, 7, 3);

        SetAffinity_Orange(1);
        SetAffinity_Light(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetPrimaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainInvocation(secondaryValue);
        PCLActions.Bottom.RemoveDebuffs(player, ListSelection.Last(0), 1).AddCallback(debuffs -> {
            if (debuffs.size() == 0) {
                PCLActions.Bottom.GainInvocation(secondaryValue);
            }
        });
    }
}