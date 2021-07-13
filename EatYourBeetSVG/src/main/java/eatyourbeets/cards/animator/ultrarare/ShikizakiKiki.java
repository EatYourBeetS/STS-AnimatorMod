package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(ShikizakiKiki.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public ShikizakiKiki()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetEthereal(true);

        SetSeries(CardSeries.Katanagatari);
        SetAffinity(1, 0, 1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber)
        .AddCallback(() ->
        {
           for (AbstractCard card : player.hand.group)
           {
               if (card instanceof EYBCard && card.type == CardType.ATTACK)
               {
                   ((EYBCard)card).agilityScaling += magicNumber;
                   ((EYBCard)card).forceScaling += magicNumber;
                   card.flash();
               }
           }
        });
    }
}