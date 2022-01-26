package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.series.NoGameNoLife.Sora;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Sora_Strategy1 extends PCLCard
{
    public static final PCLCardData DATA = Register(Sora_Strategy1.class)
            .SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Ranged, PCLCardTarget.Normal)
            .SetSeries(Sora.DATA.Series);

    public Sora_Strategy1()
    {
        super(DATA);

        Initialize(23, 0, 5);
        SetUpgrade(5,0,0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Exhaust(this);
    }

    @Override
    protected void UpdateDamage(float amount)
    {
        super.UpdateDamage(baseDamage);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.PSYCHOKINESIS).forEach(d -> d.SetVFXColor(Color.ORANGE));
    }
}