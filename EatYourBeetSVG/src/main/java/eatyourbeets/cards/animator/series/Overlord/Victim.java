package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Victim extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Victim.class)
            .SetSkill(3, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Victim()
    {
        super(DATA);

        Initialize(0,3, 38, 4);
        SetUpgrade(0,0,5,2);

        SetAffinity_Light(1);
        SetAffinity_Dark(2,0,2);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal && (GameUtilities.HasDarkAffinity(c)))
                .AddCallback(cards ->
                {
                    AbstractCard card = null;
                    if (cards.size() > 0)
                    {
                        card = cards.get(0);
                        GameUtilities.Retain(card);
                    }
                    GameActions.Bottom.StackPower(new VictimPower(p,card, magicNumber, secondaryValue));
                });

    }

    public static class VictimPower extends AnimatorPower
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
            if (damageAmount > 1 && info.type == DamageInfo.DamageType.NORMAL || info.type == DamageInfo.DamageType.THORNS) {
                if (player.currentHealth <= damageAmount && damageAmount <= this.amount && CombatStats.TryActivateLimited(Victim.DATA.ID)) {
                    this.amount -= damageAmount;
                    damageAmount = 0;
                    if (targetCard != null && targetCard.baseBlock > 0) {
                        GameUtilities.ModifyBlock(targetCard, secondaryAmount, false);
                    }
                }
                this.flash();
                GameActions.Bottom.GainBlock(this.owner,this.amount);
                GameActions.Last.RemovePower(owner, owner, this);
            }
            return super.onAttackedToChangeDamage(info, damageAmount);
        }
    }
}