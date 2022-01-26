package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.pcl.EnduranceStance;
import pinacolada.stances.pcl.InvocationStance;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SougenEsaka extends PCLCard
{
    public static final PCLCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(4, 5, 1, 1);
        SetUpgrade(2, 1, 1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Red(0, 0, 1);

        SetAffinityRequirement(PCLAffinity.Orange, 5);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.GainBlock(block);

        if (PCLGameUtilities.InStance(MightStance.STANCE_ID) || PCLGameUtilities.InStance(InvocationStance.STANCE_ID)) {
            PCLActions.Bottom.GainEndurance(secondaryValue);
        }


        if (CheckAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
                    .ShowEffect(true, true)
                    .SetOrigin(CardSelection.Top).AddCallback(() -> PCLActions.Bottom.ChangeStance(EnduranceStance.STANCE_ID));

        }
    }
}