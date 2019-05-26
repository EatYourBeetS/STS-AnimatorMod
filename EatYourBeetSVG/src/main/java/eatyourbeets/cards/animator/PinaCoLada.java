package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PinaCoLadaPower;

public class PinaCoLada extends AnimatorCard
{
    public static final String ID = CreateFullID(PinaCoLada.class.getSimpleName());

    public PinaCoLada()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0,1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            GameActionsHelper.GainBlock(p, this.block);
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PinaCoLadaPower(p, 1), 1));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(8);
        }
    }
}