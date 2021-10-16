package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Chomusuke extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Chomusuke.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Konosuba);

    public Chomusuke()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0,0,1,1);

        SetAffinity_Star(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
            .ShowEffect(true, true);

            RandomizedList<AbstractCard> cards = GameUtilities.GetRandomizedCardPool((card) -> {
                if (!(card instanceof AnimatorCard))
                {
                    return false;
                }
                return GameUtilities.IsLowCost(card) && (card.rarity == CardRarity.UNCOMMON || card.rarity == CardRarity.RARE);
            });

            GameActions.Bottom.MakeCardInHand(cards.Retrieve(rng).makeCopy());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainEnergy(2);
        GameActions.Bottom.DrawNextTurn(secondaryValue);
        GameActions.Bottom.GainEnergyNextTurn(1);
    }
}