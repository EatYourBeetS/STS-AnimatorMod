package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.HinaPower;
import eatyourbeets.utilities.GameActions;

public class HinaKagiyama extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HinaKagiyama.class).SetPower(2, CardRarity.SPECIAL);

    public HinaKagiyama()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new HinaPower(p, magicNumber));

    }
    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }
}