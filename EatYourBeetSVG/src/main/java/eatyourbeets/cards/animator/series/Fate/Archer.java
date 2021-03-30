package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.ArcherPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Archer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Archer.class).SetPower(1, CardRarity.UNCOMMON);

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new ArcherPower(p, magicNumber));

        if (isSynergizing)
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }
    }
}