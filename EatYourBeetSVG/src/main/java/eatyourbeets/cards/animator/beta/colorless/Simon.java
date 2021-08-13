package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Simon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Simon.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GurrenLagann);

    public Simon()
    {
        super(DATA);

        Initialize(5, 0, 6 , 0);
        SetUpgrade(1, 0, 0 , 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Orange(1);

        SetExhaust(true);
        SetUnique(true,true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 5 == 0)
        {
            this.AddScaling(Affinity.Red, 1);
        }

        upgradedMagicNumber = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH).AddCallback(e -> {
            RandomizedList<AbstractCard> cardsToGainAttack = new RandomizedList<>();

            for (AbstractCard card : player.hand.group)
            {
                if (card.type.equals(CardType.ATTACK) && card.baseDamage > 0)
                {
                    cardsToGainAttack.Add(card);
                }
            }

            AbstractCard card = cardsToGainAttack.Retrieve(rng);

            if (card != null)
            {
                GameUtilities.IncreaseDamage(card, e.lastDamageTaken / 2, false);
                card.flash();
            }
        });

        if (CombatStats.Affinities.GetPowerAmount(Affinity.Red) >= magicNumber && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                    .IncludeMasterDeck(true)
                    .IsCancellable(false);
        }
    }
}