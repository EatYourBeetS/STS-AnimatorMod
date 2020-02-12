package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class Caster extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Caster.class).SetSkill(1, CardRarity.UNCOMMON);

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 0, 1);
        SetUpgrade(0, 0, 0, 0);

        SetEvokeOrbCount(1);
        SetEthereal(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Dark(), true);
        GameActions.Bottom.ReduceStrength(m, secondaryValue, true).SetForceGain(true);

        if (HasSynergy())
        {
            GameActions.Bottom.Add(new EvokeOrbAction(1));
        }
    }
}