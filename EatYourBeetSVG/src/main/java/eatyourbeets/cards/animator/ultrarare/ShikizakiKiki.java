package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(ShikizakiKiki.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Katanagatari);

    public ShikizakiKiki()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber)
        .AddCallback(() ->
        {
           for (AbstractCard card : player.hand.group)
           {
               if (card.type == CardType.ATTACK)
               {
                   EYBCard c = JUtils.SafeCast(card, EYBCard.class);
                   if (c != null)
                   {
                       c.AddScaling(Affinity.Green, magicNumber);
                       c.AddScaling(Affinity.Red, magicNumber);
                       c.flash();
                   }
               }
           }
        });
    }
}