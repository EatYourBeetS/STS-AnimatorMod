package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.ModifyMagicNumberAction;
import eatyourbeets.actions.WizAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Wiz extends AnimatorCard
{
    public static final String ID = CreateFullID(Wiz.class.getSimpleName());

    public Wiz()
    {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0,2);

        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.hand.size() > 0)
        {
            AbstractDungeon.actionManager.addToBottom(new ExhaustAction(p, p, 1, false));
            AbstractDungeon.actionManager.addToBottom(new WizAction(p));
            AbstractDungeon.actionManager.addToBottom(new ModifyMagicNumberAction(this.uuid, -1));
        }

        if (this.magicNumber <= 1)
        {
            this.purgeOnUse = true;
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            int temp = this.magicNumber;

            upgradeMagicNumber(1);

            this.magicNumber = temp + 1;

            purgeOnUse = false;
        }
    }
}