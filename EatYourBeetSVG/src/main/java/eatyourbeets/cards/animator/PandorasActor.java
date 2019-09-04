package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class PandorasActor extends AnimatorCard implements StartupCard
{
    public static final String ID = CreateFullID(PandorasActor.class.getSimpleName());

    public PandorasActor()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 5);

        SetSynergy(Synergies.Overlord, true);
    }

//    @SpireOverride
//    protected void applyPowersToBlock()
//    {
//        float tmp = (float) this.baseBlock;
//        if (HasActiveSynergy())
//        {
//            tmp += magicNumber;
//        }
//
//        for (AbstractPower p : AbstractDungeon.player.powers)
//        {
//            tmp = p.modifyBlock(tmp);
//        }
//
//        if (tmp < 0.0F)
//        {
//            tmp = 0.0F;
//        }
//
//        this.block = MathUtils.floor(tmp);
//        this.isBlockModified = this.block != this.baseBlock;
//    }

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

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActionsHelper.Callback(new WaitAction(0.1f), this::AtStartup, this);

        return true;
    }

    private void AtStartup(Object state, AbstractGameAction action)
    {
        AbstractCard copy = this.makeStatEquivalentCopy();
        copy.applyPowers();
        copy.use(AbstractDungeon.player, null);
        copy.purgeOnUse = true;
        copy.freeToPlayOnce = true;
        AnimatorCard.SetLastCardPlayed(copy);
    }
}