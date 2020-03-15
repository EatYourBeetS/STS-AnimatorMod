package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.animator.ZadkielAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class Zadkiel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zadkiel.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);
    private static final int BLOCK_MULTIPLIER = 4;
    static
    {
        DATA.AddPreview(new YoshinoHimekawa(), true);
    }

    public Zadkiel()
    {
        super(DATA);

        Initialize(0, 4);
        SetCostUpgrade(-1);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(BLOCK_MULTIPLIER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < BLOCK_MULTIPLIER; i++)
        {
            GameActions.Bottom.GainBlock(block);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        int numFrost = 0;

        for(int i = 0; i < AbstractDungeon.player.orbs.size(); i++)
        {
            AbstractOrb currentOrb = AbstractDungeon.player.orbs.get(0);

            if (currentOrb != null)
            {
                if (Frost.ORB_ID.equals(currentOrb.ID))
                {
                    numFrost++;
                }

                this.addToTop(new EvokeOrbAction(1));
            }
        }

        if (numFrost >= 4)
        {
            GameActions.Bottom.Add(new ZadkielAction());
        }
    }
}