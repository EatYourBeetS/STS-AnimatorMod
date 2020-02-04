package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tatsumaki extends AnimatorCard
{
    public static final String ID = Register_Old(Tatsumaki.class);

    public Tatsumaki()
    {
        super(ID, 2, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (GameUtilities.GetUniqueOrbsCount() >= 3 && EffectHistory.TryActivateSemiLimited(this.cardID))
        {
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.ChannelOrb(new Aether(), true);

        if (upgraded)
        {
            IntellectPower.PreserveOnce();
        }
    }
}