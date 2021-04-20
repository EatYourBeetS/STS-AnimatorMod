package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.RoseDamageAction;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rose extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Rose.class).SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS);

    public Rose()
    {
        super(DATA);

        Initialize(10, 0, 2, 40);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.Reload(name, m, (enemy, cards) ->
        {
            if (cards.size() > 0 && enemy != null && !GameUtilities.IsDeadOrEscaped(enemy))
            {
                GameActions.Bottom.Add(new RoseDamageAction(enemy, this, cards.size(), damage));
            }
        });
    }
}