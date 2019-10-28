package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.Hidden;

public class IchigoKurosaki extends AnimatorCard implements Hidden
{
    public static final String ID = Register(IchigoKurosaki.class.getSimpleName());

    public IchigoKurosaki()
    {
        super(ID, -1, CardType.SKILL, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 0, 5);

        SetSynergy(Synergies.Bleach);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new IchigoBankai(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

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