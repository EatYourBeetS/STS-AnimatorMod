package eatyourbeets.cards.animator;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class PandorasActor extends AnimatorCard
{
    public static final String ID = CreateFullID(PandorasActor.class.getSimpleName());

    public PandorasActor()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 3, 4);

        SetSynergy(Synergies.Overlord, true);
    }

    @SpireOverride
    protected void applyPowersToBlock()
    {
        float tmp = (float) this.baseBlock;
        if (HasActiveSynergy())
        {
            tmp += magicNumber;
        }

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