package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Hellfire extends UnnamedCard
{
    public static final String ID = CreateFullID(Hellfire.class.getSimpleName());

    public Hellfire()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0,0, 12, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActionsHelper.ChannelOrb(new Fire(), true);
        }

        GameActionsHelper.VFX(new FlameBarrierEffect(p.hb.cX, p.hb.cY));
        for (AbstractCreature c : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, c, new BurningPower(c, p, magicNumber), magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
            upgradeSecondaryValue(1);
        }
    }
}