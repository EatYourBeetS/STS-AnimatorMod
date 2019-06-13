package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ModifyMagicNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Aqua extends AnimatorCard
{
    public static final String ID = CreateFullID(Aqua.class.getSimpleName());

    public Aqua() 
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 5);

        this.secondaryValue = this.baseSecondaryValue = 2;
        this.tags.add(CardTags.HEALING);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (this.baseMagicNumber > 0)
        {
            AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ModifyMagicNumberAction(this.uuid, -this.secondaryValue));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
            //upgradeSecondaryValue(-1);
        }
    }
}