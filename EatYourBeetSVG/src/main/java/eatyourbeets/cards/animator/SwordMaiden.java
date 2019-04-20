package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class SwordMaiden extends AnimatorCard
{
    public static final String ID = CreateFullID(SwordMaiden.class.getSimpleName());

    public SwordMaiden()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 16);

        this.exhaust = true;

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new RemoveDebuffsAction(p));
        GameActionsHelper.GainTemporaryHP(p, p, this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(6);
        }
    }
}