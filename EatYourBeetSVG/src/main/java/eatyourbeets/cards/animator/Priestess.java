package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;

public class Priestess extends AnimatorCard
{
    public static final String ID = Register(Priestess.class.getSimpleName());

    public Priestess()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 4);

        //this.tags.add(CardTags.HEALING);
        //AddExtendedDescription();

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainTemporaryHP(p, p, this.block);

//        if (magicNumber > 0 && HasActiveSynergy())
//        {
//            GameActionsHelper.ApplyPower(p, p, new RegenPower(p, this.magicNumber), this.magicNumber);
//            GameActionsHelper.AddToBottom(new ModifyMagicNumberAction(this.uuid, -1));
//        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(-1);
            upgradeBaseCost(0);
        }
    }
}