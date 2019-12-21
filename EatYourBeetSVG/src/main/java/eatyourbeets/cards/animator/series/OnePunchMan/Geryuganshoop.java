package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Geryuganshoop extends AnimatorCard
{
    public static final String ID = Register(Geryuganshoop.class.getSimpleName(), EYBCardBadge.Special);

    public Geryuganshoop()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0,2, 2);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Cycle(name, secondaryValue)
        .AddCallback(__ -> GameActions.Bottom.SelectFromPile(name, magicNumber, AbstractDungeon.player.exhaustPile)
                            .SetMessage(JavaUtilities.Format(cardData.strings.EXTENDED_DESCRIPTION[0], magicNumber))
                            .SetOptions(false, true)
                            .AddCallback(this::OnCardChosen));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            upgradeMagicNumber(1);
        }
    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        boolean limited = EffectHistory.HasActivatedLimited(this.cardID);

        if (cards != null && cards.size() > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard card : cards)
            {
                if (!limited && (card.cardID.equals(Boros.ID) || card.cardID.startsWith(Melzalgald.ID)))
                {
                    EffectHistory.TryActivateLimited(this.cardID);
                    GameActions.Bottom.MoveCard(card, p.hand, p.exhaustPile)
                    .ShowEffect(true, false);
                }
                else
                {
                    p.exhaustPile.removeCard(card);
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);
                }
            }

            GameActions.Bottom.GainEnergy(cards.size());
        }
    }
}