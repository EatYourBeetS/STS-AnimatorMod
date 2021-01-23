package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_ChannelOrb extends GenericEffect
{
    AbstractOrb orb;

    public GenericEffect_ChannelOrb(int amount, AbstractOrb orb)
    {
        this.amount = amount;
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.Channel(amount, orb.name, true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        for (int i=0; i<amount; i++)
        {
            GameActions.Bottom.ChannelOrb(orb, true);
        }
    }
}
