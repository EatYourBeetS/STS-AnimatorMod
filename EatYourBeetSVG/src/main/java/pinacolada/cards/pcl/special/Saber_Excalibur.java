package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.series.Fate.Saber;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Saber_Excalibur extends PCLCard
{
    public static final PCLCardData DATA = Register(Saber_Excalibur.class)
            .SetAttack(3, CardRarity.SPECIAL, PCLAttackType.Elemental, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeries(Saber.DATA.Series);

    public Saber_Excalibur()
    {
        super(DATA);

        Initialize(30, 20, 4, 1);
        SetUpgrade(10, 5, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(2, 0, 20);

        SetRetainOnce(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));
        PCLActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.GOLD), 0.4f).SetRealtime(true);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect((c, __) -> PCLGameEffects.List.Add(VFX.VerticalImpact(c.hb).SetColor(Color.GOLD))));
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainArtifact(magicNumber);
        PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Light, PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Light));
    }
}