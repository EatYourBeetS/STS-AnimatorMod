package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Tatsumaki extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Tatsumaki.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSeries(CardSeries.OnePunchMan);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (IntellectStance.IsActive())
        {
            GameActions.Bottom.GainOrbSlots(1);
        }

        GameActions.Bottom.ChannelOrb(new Aether());

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