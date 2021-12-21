package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AcuraTooru extends PCLCard
{
    public static final PCLCardData DATA = Register(AcuraTooru.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                data.AddPreview(new AcuraAkari(), false);
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public AcuraTooru()
    {
        super(DATA);

        Initialize(4, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Red(1);

        SetProtagonist(true);

        SetAffinityRequirement(PCLAffinity.Green, 4);
        SetHitCount(2);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (TrySpendAffinity(PCLAffinity.Green)) {
            PCLActions.Bottom.MakeCardInDrawPile(PCLGameUtilities.Imitate(new AcuraAkari()));
        }

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }
    }
}