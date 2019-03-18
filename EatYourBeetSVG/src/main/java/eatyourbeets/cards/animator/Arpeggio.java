package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Arpeggio extends AnimatorCard_Boost
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
        if (ProgressBoost())
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        }
        else
        {
            ResetBoost();
            GameActionsHelper.ChannelOrb(new Plasma(), true);
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
    protected int GetBaseBoost()
    {
        return upgraded ? 1 : 2;
    }
}