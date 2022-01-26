package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.PCLPower;
import pinacolada.stances.pcl.InvocationStance;
import pinacolada.utilities.PCLActions;

public class OrihimeInoue extends PCLCard
{
    public static final PCLCardData DATA = Register(OrihimeInoue.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage();

    public OrihimeInoue()
    {
        super(DATA);

        Initialize(0, 5, 1, 5);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 3);
        SetAffinityRequirement(PCLAffinity.Light, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.StackPower(new OrihimeInouePower(p, magicNumber));

        if (TrySpendAffinity(PCLAffinity.Red, PCLAffinity.Light)) {
            PCLActions.Bottom.ChangeStance(InvocationStance.STANCE_ID);
        }
    }

    public static class OrihimeInouePower extends PCLPower
    {
        public OrihimeInouePower(AbstractPlayer owner, int amount)
        {
            super(owner, OrihimeInoue.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            RemovePower();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (this.amount <= 0)
            {
                return damageAmount;
            }

            this.amount--;

            PCLActions.Bottom.ChannelOrb(new Fire());

            return damageAmount;
        }
    }
}