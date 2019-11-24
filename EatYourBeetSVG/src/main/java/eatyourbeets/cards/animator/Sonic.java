package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.utilities.GameActionsHelper;

public class Sonic extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Sonic.class.getSimpleName(), EYBCardBadge.Synergy);

    public Sonic()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 2, 2, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        MartialArtist.ApplyScaling(this, 6);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainAgility(secondaryValue);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(ThrowingKnife.GetRandomCard()));
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainBlock(p, block);
            GameActionsHelper.ApplyPower(p, p, new BlurPower(p, 1), 1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}