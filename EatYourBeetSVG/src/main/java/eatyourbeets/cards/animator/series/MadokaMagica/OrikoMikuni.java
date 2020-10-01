package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrikoMikuni extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrikoMikuni.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.Scry(secondaryValue);
        GameActions.Bottom.StackPower(new TemporaryRetainPower(p, magicNumber));

        if (GameUtilities.InStance(IntellectStance.STANCE_ID) && !CombatStats.HasActivatedSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1);
            CombatStats.TryActivateSemiLimited(cardID);
        }
    }
}