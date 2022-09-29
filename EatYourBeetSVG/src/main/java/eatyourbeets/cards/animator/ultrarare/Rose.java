package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.RoseDamageAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rose extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Rose.class)
            .SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Elsword);

    public Rose()
    {
        super(DATA);

        Initialize(8, 0, 2, 32);
        SetUpgrade(2, 0, 0, 8);

        SetAffinity_Red(2);
        SetAffinity_Light(1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.Reload(name, m, (enemy, cards) ->
        {
            if (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy))
            {
                GameActions.Bottom.Add(new RoseDamageAction(enemy, this, cards.size() + 1, damage));
            }
        });
    }
}