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

        Initialize(40, 0, 2, 10);
        SetUpgrade(10, 0, 2, 0);

        SetAffinity_Star(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Star(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.GetCommonOrbCount() > 1)
        {
            GameActions.Bottom.Draw(magicNumber);
        }

        GameActions.Bottom.Reload(name, m, (enemy, cards) ->
        {
            if (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy))
            {
                GameActions.Bottom.Add(new RoseDamageAction(enemy, this, cards.size() + 1, damage, secondaryValue));
            }
        });
    }
}