package pinacolada.cards.pcl.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.TanyaDegurechaff_Type95;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class TanyaDegurechaff extends PCLCard
{
    public static final PCLCardData DATA = Register(TanyaDegurechaff.class)
            .SetAttack(1, CardRarity.RARE, PCLAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.YoujoSenki)
            .PostInitialize(data -> data.AddPreview(new TanyaDegurechaff_Type95(), false));

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(7, 2, 2);
        SetUpgrade(2, 2, 0);

        SetAffinity_Silver(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.General, 11);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.75f, 0.8f));
        PCLActions.Bottom.DiscardFromPile(name, magicNumber, p.drawPile)
        .ShowEffect(true, true)
        .SetFilter(PCLGameUtilities::IsHindrance)
        .SetOptions(CardSelection.Top, true);

        if (CheckAffinity(PCLAffinity.General) && info.TryActivateLimited())
        {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                PCLActions.Bottom.MakeCardInDrawPile(new TanyaDegurechaff_Type95());
            });
        }
    }
}