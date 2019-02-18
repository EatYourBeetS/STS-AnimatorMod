package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ModifyMagicNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Caster extends AnimatorCard
{
    public static final String ID = CreateFullID(Caster.class.getSimpleName());

    public Caster()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 3);

        baseSecondaryValue = secondaryValue = 2;

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.Special(new ModifyMagicNumberAction(this.uuid, -1));
        GameActionsHelper.ApplyPower(p, m, new StrengthPower(m, -this.magicNumber), -this.magicNumber);
        if (HasActiveSynergy())
        {
            GameActionsHelper.Special(new ChannelAction(new Dark(), true));
        }

        if (this.magicNumber < 1)
        {
            this.purgeOnUse = true;
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}