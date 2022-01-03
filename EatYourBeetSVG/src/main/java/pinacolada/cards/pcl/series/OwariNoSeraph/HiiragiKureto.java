package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.utilities.PCLActions;

public class HiiragiKureto extends PCLCard
{
    public static final PCLCardData DATA = Register(HiiragiKureto.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int AMOUNT = 4;

    public HiiragiKureto()
    {
        super(DATA);

        Initialize(6, 0, AMOUNT, 15);
        SetUpgrade(0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetHitCount(3);

        SetAffinityRequirement(PCLAffinity.Light, 5);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d
                .SetVFXColor(Color.GOLDENROD)
                .AddCallback(e -> {
            if (e.lastDamageTaken > 0) {
                PCLActions.Bottom.ExhaustFromHand(name,1,true).AddCallback(
                        cards -> {
                            if (cards.size() > 0) {
                                PCLActions.Bottom.StackPower(new HiiragiKuretoPower(player, 1));
                            }
                        }
                );
            }
        }));

        if (TrySpendAffinity(PCLAffinity.Light)) {
            PCLActions.Bottom.Callback(() -> PCLCombatStats.AddEffectBonus(ElectrifiedPower.POWER_ID, secondaryValue));
        }
    }

    public class HiiragiKuretoPower extends PCLPower
    {
        public HiiragiKuretoPower(AbstractCreature owner, int amount)
        {
            super(owner, HiiragiKureto.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
        {
            super.onAttack(info, damageAmount, target);

            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
            {
                PCLActions.Top.ApplyElectrified(owner, target, AMOUNT).ShowEffect(true, true);
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, AMOUNT);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (card.type.equals(CardType.ATTACK) && action.target != null) {
                this.amount -= 1;
                if (this.amount <= 0) {
                    RemovePower();
                }
                this.flash();
            }
        }
    }
}