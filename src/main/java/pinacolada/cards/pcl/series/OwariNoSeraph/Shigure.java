package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Shigure extends PCLCard
{
    public static final PCLCardData DATA = Register(Shigure.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Green), true));

    public Shigure()
    {
        super(DATA);

        Initialize(7, 0, 2, 2);
        SetUpgrade(2, 0, 1, 0);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect(enemy -> PCLGameEffects.List.Add(VFX.DaggerSpray()).duration));
        if (CheckPrimaryCondition(false)) {
            PCLActions.Bottom.GainSupportDamage(magicNumber);
        }
        else {
            PCLActions.Bottom.ApplyPoison(p, m, magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (VelocityStance.IsActive() || info.IsSynergizing)
        {
            PCLActions.Bottom.Cycle(name, secondaryValue);
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true) > PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green, true);
    }
}