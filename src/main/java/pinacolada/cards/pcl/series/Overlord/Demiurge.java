package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Demiurge extends PCLCard
{
    public static final PCLCardData DATA = Register(Demiurge.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Demiurge()
    {
        super(DATA);

        Initialize(0,0,7, 3);
        SetUpgrade(0,0, -1, 1);

        SetAffinity_Orange(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (misc > 0)
        {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, misc, AttackEffects.DARK);
            PCLActions.Bottom.Flash(this);
            misc = 0;
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, 1, false);
        PCLActions.Bottom.GainEnergy(1);
        PCLActions.Bottom.AddAffinity(PCLAffinity.Dark, secondaryValue);
        PCLActions.Bottom.ModifyAllInstances(uuid)
        .AddCallback(c -> c.misc += magicNumber);
    }
}