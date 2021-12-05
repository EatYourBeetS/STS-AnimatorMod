package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Overlord.Albedo;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class FeridBathory extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FeridBathory.class)
            .SetPower(2, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2);

    public FeridBathory()
    {
        super(DATA);

        Initialize(0,0, 2, FeridBathoryPower.FORCE_AMOUNT);
        SetUpgrade(0, 2, 0);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetDelayed(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new FeridBathoryPower(p, magicNumber));
    }

    public class FeridBathoryPower extends AnimatorClickablePower
    {
        public static final int EXHAUST_PILE_THRESHOLD = 3;
        public static final int FORCE_AMOUNT = 1;

        public FeridBathoryPower(AbstractCreature owner, int amount)
        {
            super(owner, Albedo.DATA, PowerTriggerConditionType.Special, EXHAUST_PILE_THRESHOLD);
            this.triggerCondition.SetCheckCondition((__) -> player.exhaustPile.size() >= EXHAUST_PILE_THRESHOLD);
            this.triggerCondition.SetPayCost((cost) -> GameActions.Bottom.PurgeFromPile(name, cost).SetOptions(false, false));

            Initialize(amount);
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                    .SetDamageEffect(enemy ->
                    {
                        GameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY));
                        return 0f;
                    });
            GameActions.Bottom.GainTemporaryHP(amount);
            flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.GainMight(FORCE_AMOUNT);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, EXHAUST_PILE_THRESHOLD, FORCE_AMOUNT);
        }
    }
}