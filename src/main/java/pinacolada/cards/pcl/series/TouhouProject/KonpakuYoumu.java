package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.cards.pcl.tokens.AffinityToken_Green;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KonpakuYoumu extends PCLCard
{
    public static final PCLCardData DATA = Register(KonpakuYoumu.class).SetSkill(-1, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage(true)
            .SetMultiformData(2, false)
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Green), true));

    public KonpakuYoumu()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Green(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Green, 6);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            Initialize(0, 0, 0, 0);
            SetUpgrade(0, 0, 0, 0);
            SetRetain(true);
        }
        else {
            Initialize(0, 0, 0, 0);
            SetUpgrade(0, 0, 1, 0);
            SetRetain(false);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int xCost = PCLGameUtilities.UseXCostEnergy(this) + magicNumber;
        PCLActions.Bottom.Scry(xCost);
        PCLActions.Bottom.DrawNextTurn(xCost);
        if (xCost > 2 && CheckAffinity(PCLAffinity.Green)) {
            AffinityToken_Green token = new AffinityToken_Green();
            token.upgrade();
            PCLActions.Bottom.MakeCardInHand(token);
        }
    }
}

