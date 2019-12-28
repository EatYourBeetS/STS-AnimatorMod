package eatyourbeets.cards.animator.basic;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend_Fate extends Defend
{
    public static final String ID = Register(Defend_Fate.class);

    public Defend_Fate()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Fate);
    }

    @SpireOverride
    protected void applyPowersToBlock()
    {
        float tmp = (float) this.baseBlock;

        tmp += GameUtilities.GetCurrentEnemies(true).size() * magicNumber;

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
        GameActions.Bottom.GainBlock(this.block);
    }
}