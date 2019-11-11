package eatyourbeets.cards.animator;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class Defend_Fate extends Defend
{
    public static final String ID = Register(Defend_Fate.class.getSimpleName());

    public Defend_Fate()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);

        SetSynergy(Synergies.Fate);
    }

    @SpireOverride
    protected void applyPowersToBlock()
    {
        float tmp = (float) this.baseBlock;

        tmp += PlayerStatistics.GetCurrentEnemies(true).size() * magicNumber;

        for (AbstractPower p : AbstractDungeon.player.powers)
        {
            tmp = p.modifyBlock(tmp);
        }

        if (tmp < 0.0F)
        {
            tmp = 0.0F;
        }

        this.block = MathUtils.floor(tmp);
        this.isBlockModified = this.block != this.baseBlock;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        //int blockBonus = this.magicNumber * PlayerStatistics.GetCurrentEnemies(true).size();

        GameActionsHelper.GainBlock(p, this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}