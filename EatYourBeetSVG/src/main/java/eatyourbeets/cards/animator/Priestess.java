package eatyourbeets.cards.animator;

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
        GameActionsHelper.GainTemporaryHP(p, p, this.block);

        if (HasActiveSynergy() && ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, p, new RegenPower(p, this.magicNumber), this.magicNumber);
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

    @Override
    protected int GetBaseBoost()
    {
        return 2;
    }
}