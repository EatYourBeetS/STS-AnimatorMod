package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Priestess extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Priestess.class.getSimpleName());

    public Priestess()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 4, 2);

        this.tags.add(CardTags.HEALING);

        AddExtendedDescription();

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, p, new RegenPower(p, this.magicNumber), this.magicNumber);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new AddTemporaryHPAction(p, p, this.block));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            upgradeBlock(2);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return upgraded ? 4 : 3;
    }
}