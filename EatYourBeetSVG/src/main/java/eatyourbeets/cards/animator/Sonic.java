package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.SonicPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Sonic extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Sonic.class.getSimpleName(), EYBCardBadge.Synergy);

    public Sonic()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1, 2);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        secondaryValue = baseSecondaryValue + Math.max(0, Math.floorDiv(PlayerStatistics.GetDexterity(), 2));
        isSecondaryValueModified = (baseSecondaryValue != secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new SonicPower(p, magicNumber), magicNumber);

        GameActionsHelper.GainAgility(magicNumber);
        GameActionsHelper.DrawCard(p, secondaryValue);

        if (HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(ThrowingKnife.GetRandomCard()));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}