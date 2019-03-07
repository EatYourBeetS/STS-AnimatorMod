package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Tyuule extends AnimatorCard
{
    public static final String ID = CreateFullID(Tyuule.class.getSimpleName());

    public Tyuule()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(0,0,2);

        this.secondaryValue = this.baseSecondaryValue = 0;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        this.secondaryValue = Math.max(0, AbstractDungeon.player.hand.getSkills().size() - 1) * this.magicNumber;
        this.isSecondaryValueModified = secondaryValue > 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int poison = this.secondaryValue;
        if (poison > 0)
        {
            for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m1, p, new PoisonPower(m1, p, poison), poison));
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}