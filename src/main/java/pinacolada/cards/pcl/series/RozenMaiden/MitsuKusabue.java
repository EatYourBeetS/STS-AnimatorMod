package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.stances.InvocationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MitsuKusabue extends PCLCard
{
    public static final PCLCardData DATA =
            Register(MitsuKusabue.class)
                    .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public MitsuKusabue() {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 2, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Draw(magicNumber)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        PCLGameUtilities.ModifyCostForTurn(card, 1, true);
                        PCLGameUtilities.Retain(card);
                        if ((InvocationStance.IsActive() || CheckAffinity(PCLAffinity.Light)) && PCLGameUtilities.HasLightAffinity(card)) {
                            PCLActions.Bottom.GainTemporaryHP(secondaryValue);
                        }
                    }
                    PCLActions.Last.Add(new RefreshHandLayout());
                });
    }
}
