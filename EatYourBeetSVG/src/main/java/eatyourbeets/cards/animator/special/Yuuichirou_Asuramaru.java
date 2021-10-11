package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou_Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data -> data.AddPreview(new Status_Wound(), false));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 4, 5);
        SetUpgrade(0,0,1,0);

        SetAffinity_Fire(2);
        SetAffinity_Air(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RaiseFireLevel(magicNumber,true);
        GameActions.Bottom.RaiseAirLevel(magicNumber,true);
        GameActions.Bottom.RaiseWaterLevel(magicNumber,true);
        GameActions.Bottom.DealDamageAtEndOfTurn(player,player,secondaryValue);
        GameActions.Bottom.MakeCardInHand(new Status_Wound());
        GameActions.Bottom.MakeCardInHand(new Status_Wound());

        if (CombatStats.Affinities.GetPowerAmount(Affinity.Dark) >= secondaryValue) {
            GameActions.Bottom.StackPower(new DemonFormPower(p, magicNumber));
        }
    }
}