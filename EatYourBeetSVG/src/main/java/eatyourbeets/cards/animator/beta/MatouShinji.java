package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class MatouShinji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouShinji.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new MatouShinji_CommandSpell(), false);
    }

    public MatouShinji()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.maxOrbs == 0 && EffectHistory.TryActivateLimited(cardID)) {
            GameActions.Bottom.GainOrbSlots(3);
        } else {
            addToBot(new DecreaseMaxOrbAction(1));
            GameActions.Bottom.GainIntellect(magicNumber);
            GameActions.Bottom.MakeCardInHand(new MatouShinji_CommandSpell());
        }
    }
}
