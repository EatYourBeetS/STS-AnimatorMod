package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.cards.pcl.tokens.AffinityToken_Blue;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_JunTormented extends PCLCard_Curse implements OnPurgeSubscriber
{
    public static final PCLCardData DATA = Register(Curse_JunTormented.class)
            .SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, false).SetSeries(CardSeries.RozenMaiden);
    static
    {
        DATA.CardRarity = CardRarity.SPECIAL;
    }

    public Curse_JunTormented()
    {
        super(DATA, true);
        Initialize(0,0,2,0);
        SetAffinity_Dark(1);
        SetAffinity_Blue(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractCard c : player.hand.group) {
            if (PCLGameUtilities.IsHindrance(c) && !uuid.equals(c.uuid) && !Curse_JunTormented.DATA.ID.equals(c.cardID)) {
                PCLActions.Last.MakeCardInHand(c.makeStatEquivalentCopy());
            }
        }
    }

    @Override
    public void triggerWhenCreated(boolean isStartOfBattle)
    {
        super.triggerWhenCreated(isStartOfBattle);
        PCLCombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid) && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.MakeCardInHand(new AffinityToken_Blue());
        }
    }
}