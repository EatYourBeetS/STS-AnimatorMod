package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.PinaCoLadaPower;
import eatyourbeets.utilities.GameActions;

public class PinaCoLada extends AnimatorCard
{
    public static final EYBCardData DATA = Register(PinaCoLada.class).SetPower(2, CardRarity.RARE).SetMaxCopies(2);

    public PinaCoLada()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 6, 0);

        SetSynergy(Synergies.Gate);
        SetAlignment(0, 0, 1, 2, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new PinaCoLadaPower(p, 1));
    }
}