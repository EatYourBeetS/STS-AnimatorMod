package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.utilities.GameActionsHelper;

public class IchigoKurosaki extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(IchigoKurosaki.class.getSimpleName());

    public IchigoKurosaki()
    {
        super(ID, -1, CardType.SKILL, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 0, 5);

        SetSynergy(Synergies.Bleach);
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

    private static AbstractCard cardPreview;
    @Override
    protected AbstractCard GetCardPreview()
    {
        if (cardPreview == null)
        {
            cardPreview = new IchigoBankai();
        }

        return cardPreview;
    }
}