package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;

public class MaesHughes extends AnimatorCard
{
    public static final String ID = CreateFullID(MaesHughes.class.getSimpleName());

    public MaesHughes()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,6, 3);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, secondaryValue), secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int cardDraw = Math.floorDiv(p.drawPile.size(), magicNumber);
        GameActionsHelper.DrawCard(p, cardDraw);
        GameActionsHelper.Motivate(1, 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-1);
            upgradeSecondaryValue(2);
        }
    }
}