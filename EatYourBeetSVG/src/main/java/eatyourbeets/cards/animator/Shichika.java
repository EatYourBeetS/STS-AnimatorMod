package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shichika extends AnimatorCard
{
    public static final String ID = CreateFullID(Shichika.class.getSimpleName());

    public Shichika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 2);

        AddExtendedDescription();

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
        GameActionsHelper.GainBlock(p, block);

        if (HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new ShichikaKyotouryuu()));
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