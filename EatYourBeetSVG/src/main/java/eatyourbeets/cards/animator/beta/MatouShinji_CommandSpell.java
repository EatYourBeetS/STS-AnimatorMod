package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.CommandSpellPower;
import eatyourbeets.utilities.GameActions;

public class MatouShinji_CommandSpell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouShinji_CommandSpell.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public MatouShinji_CommandSpell()
    {
        super(DATA);
        SetCostUpgrade(0);
        SetRetain(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
       GameActions.Bottom.ApplyPower(p, p, new CommandSpellPower(p, 1), 1);
    }
}
