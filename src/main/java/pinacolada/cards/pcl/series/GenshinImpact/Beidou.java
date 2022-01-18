package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.utilities.PCLActions;

public class Beidou extends PCLCard
{
    public static final PCLCardData DATA = Register(Beidou.class).SetAttack(3, CardRarity.COMMON).SetSeriesFromClassPackage(true);

    public Beidou()
    {
        super(DATA);

        Initialize(3, 16, 3, 1);
        SetUpgrade(1, 2, 0, 0);
        SetAffinity_Red(1, 0 ,1);
        SetAffinity_Orange(1, 0 ,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new CounterAttackPower(p, magicNumber));
        PCLActions.Bottom.StackPower(new BeidouPower(p, secondaryValue));
    }

    public static class BeidouPower extends PCLPower
    {
        public BeidouPower(AbstractCreature owner, int amount)
        {
            super(owner, Beidou.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            AbstractCreature mo = info.owner;
            if (info.type == DamageInfo.DamageType.NORMAL && owner.currentBlock > 0 && mo != null)
            {
                PCLActions.Bottom.ApplyElectrified(TargetHelper.Normal(mo), amount).AddCallback(() -> {
                    for (AbstractPower po : mo.powers) {
                        if (po instanceof ElectrifiedPower) {
                            PCLActions.Bottom.Add(((ElectrifiedPower) po).Trigger());
                            break;
                        }
                    }
                });
                this.flashWithoutSound();
                RemovePower();
            }

            return super.onAttacked(info, damageAmount);
        }

        public void atStartOfTurn()
        {
            RemovePower();
        }
    }
}

