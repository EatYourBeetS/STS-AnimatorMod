package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.Arrays;

public class TomoyaOkazaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TomoyaOkazaki.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Clannad);

    private int turns;

    public TomoyaOkazaki()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(1, 0, 0);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        AffinityType highestAffinity = JUtils.FindMax(Arrays.asList(AffinityType.BasicTypes()), this::GetTeamwork);
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.discardPile, player.drawPile)
                .SetOptions(true, true)
                .SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).affinities.GetLevel(highestAffinity) > 0)
                .AddCallback(cards -> {
            for (AbstractCard card : cards)
            {
                GameActions.Bottom.Motivate(card, 1);
            }
        });

    }

}