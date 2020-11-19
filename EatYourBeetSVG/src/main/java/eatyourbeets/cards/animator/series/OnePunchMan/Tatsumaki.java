package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Tatsumaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tatsumaki.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (IntellectStance.IsActive())
        {
            GameActions.Bottom.GainOrbSlots(1);
        }

        GameActions.Bottom.ChannelOrb(new Aether(), true);

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            if (player.stance.ID.equals(NeutralStance.STANCE_ID))
            {
                GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
            }
            else
            {
                GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            }
        }
    }
}