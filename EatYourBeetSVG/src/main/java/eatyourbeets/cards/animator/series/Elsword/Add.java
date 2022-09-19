package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Add extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Add.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() >= (14 + (10 * data.GetTotalCopies(deck))))
            .PostInitialize(data -> data.AddPreviews(OrbCore.GetAllCores(), false));

    public Add()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 3, 1);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Dark, 2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainCorruption(secondaryValue);
        GameActions.Bottom.GainEnergyNextTurn(1);
        GameActions.Bottom.GainInspiration(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TryUseAffinity(Affinity.Dark))
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
            .AddCallback(this::OnCardChosen);
        }
    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            CardGroup cardGroup = null;
            final AbstractCard c = cards.get(0);
            if (player.hand.contains(c))
            {
                cardGroup = player.hand;
            }
            else if (player.drawPile.contains(c))
            {
                cardGroup = player.drawPile;
            }
            else if (player.discardPile.contains(c))
            {
                cardGroup = player.discardPile;
            }

            if (cardGroup != null)
            {
                GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1, 3)
                .AddCallback(cardGroup, this::OrbChosen));
            }
        }
    }

    private void OrbChosen(CardGroup cardGroup, ArrayList<AbstractCard> chosen)
    {
        if (cardGroup != null && chosen != null && chosen.size() == 1)
        {
            switch (cardGroup.type)
            {
                case DRAW_PILE:
                case HAND:
                case DISCARD_PILE:
                    GameActions.Bottom.MakeCard(chosen.get(0), cardGroup);
                    break;

                default:
                    JUtils.LogWarning(this, "Invalid card group type: " + cardGroup.type);
                    break;
            }
        }
    }
}