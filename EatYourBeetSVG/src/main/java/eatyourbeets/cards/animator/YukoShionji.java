package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.YukoShionjiPower;

public class YukoShionji extends AnimatorCard
{
    public static final String ID = CreateFullID(YukoShionji.class.getSimpleName());

    public YukoShionji()
    {
        super(ID, 2, CardType.POWER, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        AddExtendedDescription();

        this.isEthereal = true;

        SetSynergy(Synergies.KamisamaNoMemochu);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new YukoShionjiPower(p, 1), 1);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.isEthereal = false;
        }
    }
}