package pinacolada.cards.pcl.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Status_Wound;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Spearman extends PCLCard
{
    public static final PCLCardData DATA = Register(Spearman.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Wound(), false));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Spearman()
    {
        super(DATA);

        Initialize(12, 0, 1);
        SetUpgrade(4, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetVFXColor(Color.LIGHT_GRAY).SetSoundPitch(0.75f, 0.85f));
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Normal(m), magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TrySpendAffinity(PCLAffinity.Red, PCLAffinity.Green))
        {
            PCLActions.Bottom.MakeCardInHand(new Status_Wound());
        }
        else {
            PCLActions.Bottom.MakeCardInDrawPile(new Status_Wound())
                    .SetDestination(CardSelection.Top);
        }
    }
}