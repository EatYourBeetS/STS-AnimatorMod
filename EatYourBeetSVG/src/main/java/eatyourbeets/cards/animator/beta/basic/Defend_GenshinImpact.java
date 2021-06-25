package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Defend_GenshinImpact extends Defend
{
    public static final String ID = Register(Defend_GenshinImpact.class).ID;

    public Defend_GenshinImpact()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 4, 0);
        SetUpgrade(0, 3);

        SetCooldown(2, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Earth());
    }
}