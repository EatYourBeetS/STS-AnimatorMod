package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Tet extends AnimatorCard
{
    public static final String ID = Register(Tet.class.getSimpleName(), EYBCardBadge.Synergy);

    public Tet()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.NoGameNoLife);
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
        DiscardFromDrawPile().AddCallback(this::ShuffleFromDiscardPile);

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            SetRetain(true);
        }
    }

    private EYBActionWithCallback<ArrayList<AbstractCard>> DiscardFromDrawPile()
    {
        return GameActions.Bottom.SelectFromPile(name, magicNumber, AbstractDungeon.player.drawPile)
        .SetMessage(JavaUtilities.Format(AnimatorResources_Strings.GridSelection.TEXT[0], magicNumber))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, AbstractDungeon.player.discardPile, false);
            }
        });
    }

    private void ShuffleFromDiscardPile(ArrayList<AbstractCard> __)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, AbstractDungeon.player.discardPile)
        .SetMessage(JavaUtilities.Format(AnimatorResources_Strings.GridSelection.TEXT[1], magicNumber))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, AbstractDungeon.player.drawPile, false);
            }
        });
    }
}