package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken_Blue;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_JunTormented extends AnimatorCard_Curse implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(Curse_JunTormented.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.RozenMaiden);
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
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (AbstractCard c : player.hand.group) {
            if (GameUtilities.IsHindrance(c) && !uuid.equals(c.uuid)) {
                GameActions.Last.MakeCardInHand(c.makeStatEquivalentCopy());
            }
        }
    }

    @Override
    public void triggerWhenCreated(boolean isStartOfBattle)
    {
        super.triggerWhenCreated(isStartOfBattle);
        CombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(uuid) && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.MakeCardInHand(new AffinityToken_Blue());
        }
    }
}