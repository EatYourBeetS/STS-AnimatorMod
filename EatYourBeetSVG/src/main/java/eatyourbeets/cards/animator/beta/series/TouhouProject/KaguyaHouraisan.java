package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public class KaguyaHouraisan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaguyaHouraisan.class).SetSkill(1,CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public KaguyaHouraisan()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1);
        SetAffinity_Silver(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.drawPile.isEmpty()) {

            List<AbstractCard> cardsToPlay = new ArrayList<>(player.hand.group);

            for (AbstractCard card : cardsToPlay)
            {
                GameActions.Bottom.PlayCard(card, player.hand, null)
                        .SpendEnergy(false);
                GameActions.Last.Exhaust(card);
                GameActions.Last.Add(AffinityToken.SelectTokenAction(name, 1)
                        .SetOptions(true, false)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                GameActions.Bottom.MakeCardInDrawPile(c);
                            }
                        }));
            }
        }
        else {
            GameActions.Bottom.Scry(magicNumber);
        }

    }
}

