package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_FullmetalAlchemist extends Defend
{
    public static final String ID = Register(Defend_FullmetalAlchemist.class).ID;

    public Defend_FullmetalAlchemist()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 4, 0);
        SetUpgrade(0, 3);

        SetCooldown(1, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Frost());
    }
}