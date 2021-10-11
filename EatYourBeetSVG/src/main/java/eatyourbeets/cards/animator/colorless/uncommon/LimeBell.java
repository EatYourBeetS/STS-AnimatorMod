package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class LimeBell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LimeBell.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.AccelWorld);

    public LimeBell()
    {
        super(DATA);

        Initialize(0, 8, 4, 1);
        SetUpgrade(0, 2, 2, 0);

        SetAffinity_Light(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block)
        .AddCallback(() ->
        {
            int toConvert = Math.min(magicNumber, player.currentBlock);
            if (toConvert > 0)
            {
                player.loseBlock(toConvert, true);
                GameActions.Bottom.GainTemporaryHP(toConvert);
            }
        });

        GameActions.Bottom.Reload(name, cards -> GameActions.Bottom.RaiseLightLevel(cards.size() * secondaryValue));
    }
}