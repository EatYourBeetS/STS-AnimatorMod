package pinacolada.cards.pcl.series.Bleach;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.pcl.DesecrationStance;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashMap;
import java.util.UUID;

public class UlquiorraCifer extends PCLCard
{
    public static final PCLCardData DATA = Register(UlquiorraCifer.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Dark)
            .SetSeriesFromClassPackage(true);
    private static HashMap<UUID, Integer> buffs;

    public UlquiorraCifer()
    {
        super(DATA);

        Initialize(5, 0, 2, 3);
        SetUpgrade(2, 0, 0);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 2);

        SetHitCount(2);
        SetEthereal(true);

        SetAffinityRequirement(PCLAffinity.Green, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DARKNESS).forEach(d -> d.SetVFXColor(Color.GREEN, Color.GREEN).SetVFX(true, false));

        if (PCLGameUtilities.InStance(DesecrationStance.STANCE_ID))
        {
            PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
            PCLActions.Bottom.GainBlur(magicNumber);
        }
        else
        {
            PCLActions.Bottom.ChangeStance(DesecrationStance.STANCE_ID);
        }

        if (TrySpendAffinity(PCLAffinity.Green)) {
            PCLActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
                    .AddCallback((cards) -> {
                        if (cards.size() > 0) {
                            PCLActions.Bottom.CreateThrowingKnives(secondaryValue);
                        }
                    });
        }
    }
}