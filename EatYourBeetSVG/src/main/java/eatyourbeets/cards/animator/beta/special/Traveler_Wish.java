package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.animator.ElementalMasteryPower;
import eatyourbeets.utilities.GameActions;

public class Traveler_Wish extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Traveler_Wish.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Traveler_Wish()
    {
        super(DATA);

        Initialize(0, 0, 15, 5);
        SetUpgrade(0, 0, 10, 0);
        SetAffinity_Star(2);
        SetPermanentHaste(true);
        SetRetainOnce(true);
        SetPurge(true);
    }

    @Override
    public void OnUpgrade()
    {
        super.OnUpgrade();

        SetLoyal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainSupercharge(1, upgraded);
        GameActions.Bottom.StackPower(new ElementalMasteryPower(p, magicNumber));
    }
}