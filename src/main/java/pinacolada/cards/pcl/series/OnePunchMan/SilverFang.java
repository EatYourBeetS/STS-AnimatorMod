package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.modifiers.BlockModifiers;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;

public class SilverFang extends PCLCard
{
    public static final PCLCardData DATA = Register(SilverFang.class)
            .SetSkill(2, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeries(CardSeries.OnePunchMan);

    public SilverFang()
    {
        super(DATA);

        Initialize(0, 7, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (c.type == CardType.ATTACK)
        {
            PCLActions.Bottom.ModifyAllInstances(uuid).AddCallback(card ->
            {
                BlockModifiers.For(card).Add(1);
                card.applyPowers();
            });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        BlockModifiers.For(this).Set(0);

        if ((info.IsSynergizing || TrySpendAffinity(PCLAffinity.Red)) && info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
        }
    }
}