package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.beta.special.Zadkiel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class YoshinoHimekawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHimekawa.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Zadkiel(), true);
    }

    public YoshinoHimekawa()
    {
        super(DATA);

        Initialize(0, 4, 1, 4);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.DateALive);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, true);

        int frostCount = JUtils.Count(player.orbs, orb -> Frost.ORB_ID.equals(orb.ID));
        if (frostCount >= secondaryValue)
        {
            GameActions.Bottom.MakeCardInHand(new Zadkiel()).SetUpgrade(upgraded, false);
        }
        else
        {
            GameActions.Bottom.ChannelOrb(new Frost());
        }

        if (CombatStats.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainOrbSlots(1);
        }
    }
}