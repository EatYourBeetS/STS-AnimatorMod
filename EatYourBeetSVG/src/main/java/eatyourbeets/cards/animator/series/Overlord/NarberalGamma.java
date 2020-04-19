package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.TemporaryElectroPower;
import eatyourbeets.utilities.GameActions;

public class NarberalGamma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NarberalGamma.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public NarberalGamma()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.Overlord);
        SetShapeshifter();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Lightning(), true);

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }

        if (CombatStats.TryActivateSemiLimited(this.cardID) && !p.hasPower(ElectroPower.POWER_ID))
        {
            GameActions.Bottom.ApplyPower(p, p, new TemporaryElectroPower(p));
        }
    }
}