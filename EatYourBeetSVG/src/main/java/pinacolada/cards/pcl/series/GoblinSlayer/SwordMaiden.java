package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.ListSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SwordMaiden extends PCLCard
{
    public static final PCLCardData DATA = Register(SwordMaiden.class)
            .SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetSeriesFromClassPackage();

    public SwordMaiden()
    {
        super(DATA);

        Initialize(0, 0, 6, 6);

        SetAffinity_Orange(1);
        SetAffinity_Light(2, 0, 1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetPrimaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return heal > 0 ? HPAttribute.Instance.SetCard(this, false).SetText(heal, Colors.Cream(1)) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = PCLGameUtilities.GetHealthRecoverAmount(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.RecoverHP(secondaryValue);
        PCLActions.Bottom.RemoveDebuffs(player, ListSelection.Last(0), 1).AddCallback(debuffs -> {
            if (debuffs.size() == 0) {
                PCLActions.Bottom.GainInvocation(secondaryValue);
            }
        });
    }
}