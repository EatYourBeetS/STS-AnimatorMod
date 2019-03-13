package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.GuyAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(0,0, 4);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new GuyAction(m, p));
        GameActionsHelper.CycleCardAction(1);

//        if (ProgressCooldown())
//        {
//            GameActionsHelper.ChannelOrb(new Frost(), true);
//            //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FocusPower(m, this.magicNumber), this.magicNumber));
//        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}