package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class NiaHonjou extends PCLCard
{
    public static final PCLCardData DATA = Register(NiaHonjou.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();

    public NiaHonjou()
    {
        super(DATA);

        Initialize(0, 3, 2, 2);
        SetUpgrade(0,0,0,0);
        SetAffinity_Light(1, 0, 2);
        SetAffinity_Blue(1, 0, 0);

        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyPower(new NiaHonjouPower(p, magicNumber));
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Top.GainTemporaryHP(secondaryValue);
    }

    public static class NiaHonjouPower extends PCLPower implements OnSynergySubscriber
    {
        public NiaHonjouPower(AbstractPlayer owner, int amount)
        {
            super(owner, NiaHonjou.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
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

        @Override
        public void OnSynergy(AbstractCard card) {
            PCLActions.Bottom.GainBlock(amount);
        }
    }
}