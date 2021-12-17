package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KaedeAkamatsu extends PCLCard
{
    public static final PCLCardData DATA = Register(KaedeAkamatsu.class).SetColor(CardColor.COLORLESS)
    		.SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None, true).SetSeries(CardSeries.Danganronpa);

    public KaedeAkamatsu()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetAffinity_Light(1, 0, 1);

        SetEthereal(true);
        SetExhaust(true);

        SetHarmonic(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(new KaedeAkamatsuPower(p, magicNumber));

        PCLActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
                .SetFilter(PCLGameUtilities::HasBlueAffinity)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        PCLActions.Top.MoveCard(c, player.exhaustPile, player.hand)
                                .ShowEffect(true, true);
                    }
                });
    }

    public static class KaedeAkamatsuPower extends PCLPower implements OnCardCreatedSubscriber
    {

        public KaedeAkamatsuPower(AbstractCreature owner, int amount)
        {
            super(owner, KaedeAkamatsu.DATA);

            this.amount = amount;

            updateDescription();
            PCLCombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle)
        {
            if (!PCLGameUtilities.IsHindrance(card) && PCLGameUtilities.IsHighCost(card))
            {
                if (this.TryActivate())
                {
                    card.modifyCostForCombat(-1);
                    card.flash();
                    card.update();
                    flash();

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
