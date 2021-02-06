package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.PridePower;
import eatyourbeets.utilities.GameActions;

public class Pride extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Pride.class).SetSkill(2, CardRarity.UNCOMMON);

    public Pride()
    {
        super(DATA);

        Initialize(0,0, 1, 3);
        SetUpgrade(0, 0, 1);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
        SetShapeshifter();
    }

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }

        GameActions.Bottom.ApplyConstricted(p, m, this.secondaryValue);
        GameActions.Bottom.ApplyPower(new PridePower(p));
    }
}