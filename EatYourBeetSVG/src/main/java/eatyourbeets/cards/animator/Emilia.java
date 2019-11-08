package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Emilia extends AnimatorCard
{
    public static final String ID = Register(Emilia.class.getSimpleName());

    public Emilia()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetExhaust(true);
        SetSynergy(Synergies.ReZero);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Frost(), true);
        GameActionsHelper.AddToBottom(new EmiliaAction());
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }

    private class EmiliaAction extends AnimatorAction
    {
        @Override
        public void update()
        {
            for (AbstractOrb orb : AbstractDungeon.player.orbs)
            {
                if (orb != null && Frost.ORB_ID.equals(orb.ID))
                {
                    GameActionsHelper.ChannelOrb(new Lightning(), true);
                }
            }

            this.isDone = true;
        }
    }
}