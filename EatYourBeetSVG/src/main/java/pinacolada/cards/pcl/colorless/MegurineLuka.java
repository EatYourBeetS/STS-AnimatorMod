package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MegurineLuka extends PCLCard
{
    public static final PCLCardData DATA = Register(MegurineLuka.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Vocaloid);

    public MegurineLuka()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetCostUpgrade(-1);
        SetHarmonic(true);
        SetExhaust(true);

        SetAffinity_Blue(1);
        SetAffinity_Silver(1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final AbstractCard last = PCLGameUtilities.GetLastCardPlayed(true);
        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(false, false, false)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> c instanceof PCLCard && ((PCLCard) c).series != null && !c.hasTag(HARMONIC))
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        PCLActions.Bottom.ModifyTag(card,HARMONIC,true);
                    }
                });
        if (info.IsSynergizing && last != null && last.cardID.equals(HatsuneMiku.DATA.ID) && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.ModifyTag(player.discardPile,999,HARMONIC,true).SetFilter(c -> c instanceof PCLCard && ((PCLCard) c).series != null);
        }

    }
}