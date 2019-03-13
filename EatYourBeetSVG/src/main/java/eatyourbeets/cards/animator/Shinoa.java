package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shinoa extends AnimatorCard
{
    public static final String ID = CreateFullID(Shinoa.class.getSimpleName());

    public Shinoa()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0,7, 1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        for (AbstractMonster m2 : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m2, p, new VulnerablePower(m2, this.magicNumber, false), this.magicNumber));
            if (HasActiveSynergy())
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m2, p, new WeakPower(m2, this.magicNumber, false), this.magicNumber));
            }
        }
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