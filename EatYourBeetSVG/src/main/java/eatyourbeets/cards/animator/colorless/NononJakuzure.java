package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.OnSynergySubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;

public class NononJakuzure extends AnimatorCard implements OnSynergySubscriber
{
    public static final String ID = Register(NononJakuzure.class.getSimpleName(), EYBCardBadge.Special);

    public NononJakuzure()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.KillLaKill);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (AbstractDungeon.player.hand.contains(this))
        {
            PlayerStatistics.onSynergy.Subscribe(this);
        }
    }

    @Override
    public void OnSynergy(AnimatorCard card)
    {
        if (!AbstractDungeon.player.hand.contains(this))
        {
            PlayerStatistics.onSynergy.Unsubscribe(this);
        }
        else
        {
            baseMagicNumber = (magicNumber += 1);

            flash();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Motivate(magicNumber);
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