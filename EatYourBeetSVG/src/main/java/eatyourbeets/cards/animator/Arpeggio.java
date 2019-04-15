package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class Arpeggio extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(Arpeggio.class.getSimpleName());

    public Arpeggio()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
        else
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-1);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return upgraded ? 1 : 2;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Plasma(), true);
    }
}