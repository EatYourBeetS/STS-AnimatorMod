package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class GarbageDoll extends PCLCard
{
    public static final PCLCardData DATA = Register(GarbageDoll.class).SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Clannad);

    private int turns;

    public GarbageDoll()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Silver(1);
        SetExhaust(true);
        SetAfterlife(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.exhaustPile)
                .SetMessage(PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], magicNumber))
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
                    PCLGameEffects.Queue.ShowCardBriefly(card.makeStatEquivalentCopy());
                    ((Ushio) card).SetAfterlife(true, true);
                }
                else
                {
                    player.exhaustPile.removeCard(card);
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.2f);
                }

                PCLCard aCard = PCLJUtils.SafeCast(card, PCLCard.class);
                if (aCard != null) {
                    final CardGroup possiblePicks = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
                    if (aCard.affinities.GetLevel(PCLAffinity.Star) > 0) {
                        for (AbstractCard possibleCard : player.discardPile.group)
                        {
                            if (possibleCard instanceof PCLCard && PCLJUtils.Find(((PCLCard) possibleCard).affinities.List, a -> a.level > 0) != null)
                            {
                                possiblePicks.addToBottom(possibleCard);
                            }
                        }
                    }
                    else {
                        ArrayList<PCLCardAffinity> aCardAffinities = PCLJUtils.Filter(aCard.affinities.List, a -> a.level > 0);
                        for (AbstractCard possibleCard : player.discardPile.group) {
                            for (PCLCardAffinity aCardAffinity : aCardAffinities) {
                                if (possibleCard instanceof PCLCard && ((PCLCard) possibleCard).affinities.GetLevel(aCardAffinity.type) > 0) {
                                    possiblePicks.addToBottom(possibleCard);
                                }
                            }
                        }
                    }

                    AbstractCard playCard = PCLGameUtilities.GetRandomElement(possiblePicks.group);
                    if (playCard != null) {
                        PCLActions.Top.PlayCard(PCLGameUtilities.GetRandomElement(possiblePicks.group), PCLGameUtilities.GetRandomEnemy(true));
                    }
                }
            }
        }
    }
}