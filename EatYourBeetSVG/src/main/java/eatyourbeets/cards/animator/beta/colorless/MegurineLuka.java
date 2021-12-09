package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MegurineLuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MegurineLuka.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Vocaloid);

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
        final AbstractCard last = GameUtilities.GetLastCardPlayed(true);
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(false, false, false)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).series != null && !c.hasTag(HARMONIC))
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        GameActions.Bottom.ModifyTag(card,HARMONIC,true);
                    }
                });
        if (info.IsSynergizing && last != null && last.cardID.equals(HatsuneMiku.DATA.ID) && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.ModifyTag(player.discardPile,999,HARMONIC,true).SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).series != null);
        }

    }
}