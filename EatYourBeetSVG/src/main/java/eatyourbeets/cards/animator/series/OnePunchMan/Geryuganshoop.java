package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Geryuganshoop extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Geryuganshoop.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Geryuganshoop()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Cycle(name, magicNumber)
        .AddCallback(() -> GameActions.Bottom.SelectFromPile(name, secondaryValue, player.exhaustPile)
                           .SetMessage(JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue))
                           .SetOptions(false, true)
                           .AddCallback(this::OnCardChosen));
    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        boolean limited = CombatStats.HasActivatedLimited(this.cardID);

        if (cards != null && cards.size() > 0)
        {
            for (AbstractCard card : cards)
            {
                if (!limited && (card.cardID.equals(Boros.DATA.ID) || card.cardID.startsWith(Melzalgald.DATA.ID)))
                {
                    CombatStats.TryActivateLimited(this.cardID);
                    GameActions.Bottom.MoveCard(card, player.exhaustPile, player.hand)
                            .ShowEffect(true, false);
                }
                else
                {
                    player.exhaustPile.removeCard(card);
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.2f);
                }
            }

            GameActions.Bottom.GainEnergy(cards.size());
        }
    }
}