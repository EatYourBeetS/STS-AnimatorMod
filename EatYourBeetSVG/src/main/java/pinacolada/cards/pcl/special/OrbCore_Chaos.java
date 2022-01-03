package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Chaos;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class OrbCore_Chaos extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Chaos.class, GR.Tooltips.Chaos, GR.Tooltips.Multicolor, GR.Tooltips.Affinity_General)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Chaos()
    {
        super(DATA, 1, 9);

        SetAffinity_Star(1);
    }

    @Override
    public PCLAffinity GetAffinity() {
        return PCLAffinity.General;
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Chaos.class;
    }

    @Override
    public boolean EvokeEffect(OrbCorePower power) {
        return DoAction();
    }

    @Override
    public boolean PassiveEffect(OrbCorePower power) {
        return DoAction();
    }

    public boolean DoAction() {
        AbstractCard c = PCLGameUtilities.GetCardPoolInCombat(CardRarity.COMMON).Retrieve(rng);
        if (c != null)
        {
            PCLActions.Bottom.MakeCardInHand(c).AddCallback(ca -> {
                PCLActions.Bottom.Motivate(ca, 1);
            });
            return true;
        }
        return false;
    }
}