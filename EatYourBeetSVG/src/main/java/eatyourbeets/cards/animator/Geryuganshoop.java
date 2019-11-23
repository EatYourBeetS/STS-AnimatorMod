package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ChooseAnyNumberFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
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
        String message = JavaUtilities.Format(cardData.strings.EXTENDED_DESCRIPTION[0], magicNumber);

        GameActionsHelper.CycleCardAction(this.secondaryValue, name);
        GameActionsHelper.AddToBottom(new ChooseAnyNumberFromPileAction(magicNumber, p.exhaustPile, this::OnCardChosen, this, message, true));
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

    private void OnCardChosen(Object state, ArrayList<AbstractCard> cards)
    {
        boolean limited = EffectHistory.HasActivatedLimited(this.cardID);

        if (state == this && cards != null && cards.size() > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard card : cards)
            {
                if (!limited && (card.cardID.equals(Boros.ID) || card.cardID.startsWith(Melzalgald.ID)))
                {
                    EffectHistory.TryActivateLimited(this.cardID);
                    GameActionsHelper.AddToBottom(new MoveSpecificCardAction(card, p.hand, p.exhaustPile, true));
                }
                else
                {
                    p.exhaustPile.removeCard(card);
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);
                }
            }

            GameActionsHelper.GainEnergy(cards.size());
        }
    }
}