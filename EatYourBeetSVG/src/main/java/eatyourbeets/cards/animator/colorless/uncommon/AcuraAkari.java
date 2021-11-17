package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.replacement.TemporaryEnvenomPower;
import eatyourbeets.utilities.GameActions;

public class AcuraAkari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraAkari.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(3)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public AcuraAkari()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 1, 0);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .AddCallback(() -> GameActions.Bottom.CreateThrowingKnives(magicNumber).SetUpgrade(upgraded));

        if (info.IsSynergizing)
        {
            GameActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }
}