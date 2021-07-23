package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class GarbageDoll extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GarbageDoll.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Clannad);

    private int turns;

    public GarbageDoll()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1, 0, 0);
        SetPurge(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.exhaustPile)
                .SetMessage(JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], magicNumber))
                .SetOptions(false, true)
                .AddCallback(this::OnCardChosen);

    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        boolean limited = CombatStats.HasActivatedLimited(this.cardID);

        if (cards != null && cards.size() > 0)
        {
            for (AbstractCard card : cards)
            {
                if (!limited && (card.cardID.equals(Ushio.DATA.ID)))
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

                if (card instanceof AnimatorCard) {
                    AnimatorCard aCard = JUtils.SafeCast(card, AnimatorCard.class);
                    for (AffinityType affinity : AffinityType.BasicTypes()) {
                        if (aCard.affinities.GetLevel(affinity) > 0 || aCard.affinities.GetLevel(AffinityType.Star) > 0) {
                            GameActions.Bottom.StackAffinityPower(affinity, 1, false);
                        }
                    }
                }
            }
        }
    }
}