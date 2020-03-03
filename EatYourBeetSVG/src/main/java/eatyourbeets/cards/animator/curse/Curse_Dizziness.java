package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;

public class Curse_Dizziness extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Dizziness.class).SetCurse(-2, EYBCardTarget.None);

    public Curse_Dizziness() {
        super(DATA);
        Initialize(0, 0, 0);
        SetSynergy(Synergies.TouhouProject);
        SetLoyal(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }
}