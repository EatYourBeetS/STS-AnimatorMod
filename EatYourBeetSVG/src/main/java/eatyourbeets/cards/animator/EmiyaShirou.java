package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.powers.EmiyaShirouPower;
import eatyourbeets.powers.PlayerStatistics;

public class EmiyaShirou extends AnimatorCard
{
    public static final String ID = CreateFullID(EmiyaShirou.class.getSimpleName());

    public EmiyaShirou()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0,0, 8);

        AddExtendedDescription();

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        RandomizedList<AbstractCreature> enemiesWithoutBlock = new RandomizedList<>();
        RandomizedList<AbstractCreature> enemiesWithBlock = new RandomizedList<>();
        for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m1.currentBlock <= 0)
            {
                enemiesWithoutBlock.Add(m1);
            }
            else
            {
                enemiesWithBlock.Add(m1);
            }
        }

        AbstractCreature target = null;
        if (enemiesWithoutBlock.Count() > 0)
        {
            target = enemiesWithoutBlock.Retrieve(AbstractDungeon.miscRng);
        }
        else if (enemiesWithBlock.Count() > 0)
        {
            target = enemiesWithBlock.Retrieve(AbstractDungeon.miscRng);
        }

        if (target != null)
        {
            GameActionsHelper.GainBlock(target, this.magicNumber);
            GameActionsHelper.ApplyPower(p, target, new VulnerablePower(target, 1, false), 1);
        }

        GameActionsHelper.ApplyPower(p, p, new EmiyaShirouPower(p), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-4);
        }
    }
}