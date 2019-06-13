package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class Chung extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(Chung.class.getSimpleName());

    public Chung()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0, 8);

        this.baseSecondaryValue = this.secondaryValue = 2;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Plasma(), true);
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
}