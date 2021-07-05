package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.HiteiPower;
import eatyourbeets.utilities.GameActions;

public class Hitei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hitei.class).SetPower(1, CardRarity.UNCOMMON);

    public Hitei()
    {
        super(DATA);

        Initialize(0, 0, 0, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.Katanagatari);
        SetAffinity(0, 0, 1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, upgraded));
    }
}