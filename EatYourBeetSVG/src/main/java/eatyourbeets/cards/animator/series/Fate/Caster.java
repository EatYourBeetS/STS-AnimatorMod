package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class).SetSkill(1, CardRarity.UNCOMMON);

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    protected void OnHoveringTarget(AbstractMonster mo)
    {
        GameUtilities.ModifyIntentsPreviewShackles(TargetHelper.Normal(mo), magicNumber);
    }

    @Override
    public void triggerOnExhaust()
    {
        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        GameActions.Bottom.GainForce(1, false);
        GameActions.Bottom.GainIntellect(1, false);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }
    }
}