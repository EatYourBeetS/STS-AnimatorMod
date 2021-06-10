package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rayneshia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rayneshia.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Rayneshia()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return (GameUtilities.IsCurseOrStatus(other)) || (other.exhaust) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(magicNumber)
        .SetFilter(this::WouldSynergize, false);
    }
}