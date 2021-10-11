package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.ElementalMasteryPower;
import eatyourbeets.utilities.GameActions;

public class Traveler_Wish extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Traveler_Wish.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);

    public Traveler_Wish()
    {
        super(DATA);

        Initialize(0, 0, 15, 5);
        SetUpgrade(0, 0, 5, 0);
        SetAffinity_Star(2);
        SetPermanentHaste(true);
        SetRetainOnce(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RaiseLightLevel(1, upgraded);
        GameActions.Bottom.StackPower(new ElementalMasteryPower(p, magicNumber));
    }
}