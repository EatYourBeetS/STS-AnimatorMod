package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Nutcracker extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Nutcracker.class).SetCurse(-2, EYBCardTarget.None);

    public Curse_Nutcracker()
    {
        super(DATA, true);

        Initialize(0, 0, 3);

        SetSynergy(Synergies.YoujoSenki);
        SetAffinity_Dark(1);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (dontTriggerOnUseCard)
        {
            for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.Add(new HealAction(m1, null, magicNumber));
            }
        }
    }
}