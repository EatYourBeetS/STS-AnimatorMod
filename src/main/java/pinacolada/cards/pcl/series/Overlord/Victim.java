package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Victim extends PCLCard
{
    public static final PCLCardData DATA = Register(Victim.class)
            .SetSkill(3, CardRarity.RARE, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Victim()
    {
        super(DATA);

        Initialize(0,3, 38, 4);
        SetUpgrade(0,0,5,2);

        SetAffinity_Light(1);
        SetAffinity_Dark(1,0,2);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal && (PCLGameUtilities.HasDarkAffinity(c)))
                .AddCallback(cards ->
                {
                    AbstractCard card = null;
                    if (cards.size() > 0)
                    {
                        card = cards.get(0);
                        PCLGameUtilities.Retain(card);
                    }
                    PCLActions.Bottom.StackPower(new VictimPower(p,card, magicNumber, secondaryValue));
                });

    }

    public static class VictimPower extends PCLPower
    {
        private AbstractCard targetCard;
        private int secondaryAmount;

        public VictimPower(AbstractCreature owner, AbstractCard targetCard, int amount, int secondaryAmount)
        {
            super(owner, Victim.DATA);
            this.targetCard = targetCard;
            this.secondaryAmount = secondaryAmount;

            Initialize(amount);
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
            if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL || info.type == DamageInfo.DamageType.THORNS) {
                if (player.currentHealth <= damageAmount && damageAmount <= this.amount && CombatStats.TryActivateLimited(Victim.DATA.ID)) {
                    this.amount -= damageAmount;
                    damageAmount = 0;
                    if (targetCard != null && targetCard.baseBlock > 0) {
                        PCLGameUtilities.ModifyBlock(targetCard, secondaryAmount, false);
                    }
                }
                this.flash();
                PCLActions.Bottom.GainBlock(this.owner,this.amount);
                PCLActions.Last.RemovePower(owner, owner, this);
            }
            return super.onAttackedToChangeDamage(info, damageAmount);
        }
    }
}