package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken_Blue;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_JunTormented extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_JunTormented.class)
            .SetCurse(-2, PCLCardTarget.None, true).SetSeries(CardSeries.RozenMaiden);

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
    public void triggerOnPurge() {
        if (CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.MakeCardInHand(new AffinityToken_Blue());
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}