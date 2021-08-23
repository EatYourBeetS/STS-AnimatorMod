package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class GarbageDoll extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GarbageDoll.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Clannad);

    private int turns;

    public GarbageDoll()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(1, 0, 0);
        SetExhaust(true);
        AfterLifeMod.Add(this);
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
                    GameEffects.Queue.ShowCardBriefly(card);
                    AfterLifeMod.Add(card);
                    AfterLifeMod.AfterlifeAddToControlPile(card);
                }
                else
                {
                    player.exhaustPile.removeCard(card);
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.2f);
                }

                if (card instanceof AnimatorCard) {
                    AnimatorCard aCard = JUtils.SafeCast(card, AnimatorCard.class);

                    final CardGroup possiblePicks = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
                    if (aCard.affinities.GetLevel(Affinity.Star) > 0) {
                        for (AbstractCard possibleCard : player.discardPile.group)
                        {
                            if (possibleCard instanceof AnimatorCard && JUtils.Find(((AnimatorCard) possibleCard).affinities.List, a -> a.level > 0) != null)
                            {
                                possiblePicks.addToBottom(possibleCard);
                            }
                        }
                    }
                    else {
                        ArrayList<EYBCardAffinity> aCardAffinities = JUtils.Filter(aCard.affinities.List, a -> a.level > 0);
                        for (AbstractCard possibleCard : player.discardPile.group) {
                            for (EYBCardAffinity aCardAffinity : aCardAffinities) {
                                if (possibleCard instanceof AnimatorCard && ((AnimatorCard) possibleCard).affinities.GetLevel(aCardAffinity.type) > 0) {
                                    possiblePicks.addToBottom(possibleCard);
                                }
                            }
                        }
                    }

                    AbstractCard playCard = GameUtilities.GetRandomElement(possiblePicks.group);
                    if (playCard != null) {
                        GameActions.Top.PlayCard(GameUtilities.GetRandomElement(possiblePicks.group), GameUtilities.GetRandomEnemy(true));
                    }
                }
            }
        }
    }
}