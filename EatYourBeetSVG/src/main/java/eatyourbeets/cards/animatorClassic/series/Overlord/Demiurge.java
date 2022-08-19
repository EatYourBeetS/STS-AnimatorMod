package eatyourbeets.cards.animatorClassic.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Demiurge extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Demiurge.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Demiurge()
    {
        super(DATA);

        Initialize(0,0,4);

        SetSeries(CardSeries.Overlord);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded)
        {
            GameActions.Bottom.DiscardFromHand(name, 1, false)
            .SetOptions(true, true, true)
            .AddCallback(cards -> ExecuteEffect(cards.isEmpty()));
        }
        else
        {
            ExecuteEffect(true);
        }

        if (info.IsSynergizing && !p.hasPower(ConservePower.POWER_ID))
        {
            GameActions.Bottom.StackPower(new ConservePower(p, 1));
        }
    }

    private void ExecuteEffect(boolean takeDamage)
    {
        GameActions.Bottom.GainEnergy(1);

        if (takeDamage)
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(magicNumber);
        }
    }
}