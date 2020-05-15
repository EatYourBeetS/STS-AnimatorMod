package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

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

        Initialize(0, 6, 1,4);
        SetEthereal(true);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, true);

        int frostCount = JavaUtilities.Count(player.orbs, orb -> Frost.ORB_ID.equals(orb.ID));
        if (frostCount >= secondaryValue)
        {
            GameActions.Bottom.MakeCardInHand(new Zadkiel()).SetUpgrade(upgraded, false);
        }
        else
        {
            GameActions.Bottom.ChannelOrb(new Frost(), true);
        }

        if (CombatStats.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainOrbSlots(1);
        }
    }
}