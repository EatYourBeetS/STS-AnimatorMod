package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.status.Status_Slimed;
import pinacolada.utilities.PCLActions;

public class Suiseiseki extends PCLCard
{
    public static final PCLCardData DATA = Register(Suiseiseki.class)
    		.SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Slimed(), false).AddPreview(new Souseiseki(), false));

    public Suiseiseki()
    {
        super(DATA);

        Initialize(0, 7, 4, 3);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Orange, 7);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        Status_Slimed slimed = new Status_Slimed();
        if (CheckAffinity(PCLAffinity.Orange)) {
            slimed.retain = true;
        }
        PCLActions.Bottom.MakeCard(slimed, player.drawPile);

        if (info.GetPreviousCardID().equals(Souseiseki.DATA.ID) && info.TryActivateLimited())
        {
            PCLActions.Top.PlayCopy(this, m);
        }
    }
}
