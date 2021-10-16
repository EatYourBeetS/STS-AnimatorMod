package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KaedeAkamatsu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaedeAkamatsu.class).SetColor(CardColor.COLORLESS)
    		.SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.Danganronpa);

    public KaedeAkamatsu()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetAffinity_Light(1, 0, 0);

        SetEthereal(true);
        SetExhaust(true);

        SetProtagonist(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new KaedeAkamatsuPower(p, magicNumber));

        if (info.IsSynergizing && info.Synergies.GetLevel(Affinity.Light, true) > 1)
        {
            GameActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
                    .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            GameActions.Top.MoveCard(c, player.exhaustPile, player.hand)
                                    .ShowEffect(true, true);
                        }
                    });
        }
    }

    public static class KaedeAkamatsuPower extends AnimatorPower implements OnCardCreatedSubscriber
    {

        public KaedeAkamatsuPower(AbstractCreature owner, int amount)
        {
            super(owner, KaedeAkamatsu.DATA);

            this.amount = amount;

            updateDescription();
            CombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle)
        {
            if (!GameUtilities.IsHindrance(card) && GameUtilities.IsHighCost(card))
            {
                if (this.TryActivate())
                {
                    card.modifyCostForCombat(-1);
                    card.flash();
                    card.update();

                    if (this.amount == 0)
                    {
                        this.CheckAndRemove();
                    }
                }
            }
        }

        private void CheckAndRemove()
        {
            if (this.amount == 0)
                this.RemovePower();
        }

        public boolean TryActivate()
        {
            if (this.amount >= 1)
            {
                this.amount --;

                return true;
            }

            return false;
        }
    }
}
