package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class YoshinoHimekawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YoshinoHimekawa.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Zadkiel(), true);
    }

    public YoshinoHimekawa()
    {
        super(DATA);

        Initialize(0, 4);
        SetEthereal(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, 1, true);

        if (GetFrostOrbCount() >= 4)
        {
            GameActions.Bottom.MakeCardInHand(new Zadkiel()).SetUpgrade(upgraded, false);
        }
        else
        {
            GameActions.Bottom.ChannelOrb(new Frost(), true);
        }

        if (EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainOrbSlots(1);
        }
    }

    private int GetFrostOrbCount()
    {
        int count = 0;

        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            if (orb != null && Frost.ORB_ID.equals(orb.ID))
            {
                count++;
            }
        }

        return count;
    }
}