package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.cards.pcl.series.GoblinSlayer.GoblinSlayer;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class GoblinSoldier extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(GoblinSoldier.class)
            .SetStatus(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinSoldier()
    {
        super(DATA, true);

        Initialize(0, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.Draw(1);
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            PCLActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL);
        }
    }
}