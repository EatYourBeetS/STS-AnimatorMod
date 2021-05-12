package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zadkiel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zadkiel.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public Zadkiel()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetCostUpgrade(-1);

        SetExhaust(true);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainOrbSlots(1);
        }

        GameActions.Bottom.ChannelOrbs(Frost::new, JUtils.Count(player.orbs, o -> EmptyOrbSlot.ORB_ID.equals(o.ID)));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }
}