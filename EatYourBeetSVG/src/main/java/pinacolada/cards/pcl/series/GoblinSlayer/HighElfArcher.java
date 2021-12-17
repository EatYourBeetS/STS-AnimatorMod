package pinacolada.cards.pcl.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class HighElfArcher extends PCLCard
{
    public static final PCLCardData DATA = Register(HighElfArcher.class)
            .SetAttack(0, CardRarity.COMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public HighElfArcher()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(1, 0);

        SetAffinity_Green(2, 0, 1);

        SetAffinityRequirement(PCLAffinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SFX(SFX.PCL_ARROW);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.15f).SetColor(Color.TAN)).duration * 0.5f));

        if (info.IsStarter)
        {
            PCLActions.Bottom.Draw(1);
        }

        if (info.IsSynergizing || TrySpendAffinity(PCLAffinity.Green))
        {
            PCLActions.Bottom.ApplyPoison(player, m, magicNumber);
        }
    }
}