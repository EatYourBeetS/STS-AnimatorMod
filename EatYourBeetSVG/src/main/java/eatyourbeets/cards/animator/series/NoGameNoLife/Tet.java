package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Tet extends AnimatorCard
{
    public static final String ID = Register(Tet.class, EYBCardBadge.Synergy);

    public Tet()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (upgraded)
        {
            SetRetain(true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ShuffleFromDiscardPile();
        DiscardFromDrawPile();

        if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }

    private void DiscardFromDrawPile()
    {
        GameActions.Top.SelectFromPile(name, magicNumber, player.drawPile)
        .SetMessage(AnimatorResources_Strings.GridSelection.TEXT[0], magicNumber)
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.discardPile);
            }
        });
    }

    private void ShuffleFromDiscardPile()
    {
        GameActions.Top.SelectFromPile(name, magicNumber, player.discardPile)
        .SetMessage(AnimatorResources_Strings.GridSelection.TEXT[1], magicNumber)
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.drawPile);
            }
        });
    }
}