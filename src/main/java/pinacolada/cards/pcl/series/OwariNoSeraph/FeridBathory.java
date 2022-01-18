package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.vfx.megacritCopy.HemokinesisEffect2;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class FeridBathory extends PCLCard
{
    public static final PCLCardData DATA = Register(FeridBathory.class)
            .SetPower(2, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2);

    public FeridBathory()
    {
        super(DATA);

        Initialize(0,0, 2, FeridBathoryPower.FORCE_AMOUNT);
        SetUpgrade(0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetDelayed(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new FeridBathoryPower(p, magicNumber));
    }

    public static class FeridBathoryPower extends PCLClickablePower
    {
        public static final int EXHAUST_PILE_THRESHOLD = 2;
        public static final int FORCE_AMOUNT = 1;

        public FeridBathoryPower(AbstractCreature owner, int amount)
        {
            super(owner, FeridBathory.DATA, PowerTriggerConditionType.Special, EXHAUST_PILE_THRESHOLD);
            this.triggerCondition.SetCheckCondition((__) -> player.exhaustPile.size() >= EXHAUST_PILE_THRESHOLD);
            this.triggerCondition.SetPayCost((cost) -> PCLActions.Bottom.PurgeFromPile(name, cost, player.exhaustPile).SetOptions(false, false));

            Initialize(amount);
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            PCLActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                    .SetDamageEffect(enemy ->
                    {
                        PCLGameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY));
                        return 0f;
                    });
            PCLActions.Bottom.GainTemporaryHP(amount);
            flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.GainMight(FORCE_AMOUNT);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, EXHAUST_PILE_THRESHOLD, FORCE_AMOUNT);
        }
    }
}