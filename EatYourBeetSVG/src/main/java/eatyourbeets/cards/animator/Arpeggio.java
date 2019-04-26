package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Earth;

public class Arpeggio extends AnimatorCard
{
    public static final String ID = CreateFullID(Arpeggio.class.getSimpleName());

    public Arpeggio()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0);

        this.exhaust = true;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Earth(), true);

        if (upgraded)
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        }

        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }
}