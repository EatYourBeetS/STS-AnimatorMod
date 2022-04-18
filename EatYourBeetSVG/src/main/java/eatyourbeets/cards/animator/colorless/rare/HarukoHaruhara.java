package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;

public class HarukoHaruhara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HarukoHaruhara.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FLCL);

    public HarukoHaruhara()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.THUNDERCLAP, 3.3f, 3.3f, 0.9f);
        GameActions.Bottom.SFX(SFX.BELL, 5f,5f, 0.9f);

        GameActions.Bottom.DiscardFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(cards -> GameActions.Top.Draw(cards.size()));
        GameActions.Bottom.GainEnergy(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new RandomCostIncrease(1, false));
    }
}