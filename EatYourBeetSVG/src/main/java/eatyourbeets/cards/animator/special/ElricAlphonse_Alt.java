package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricAlphonse;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonse_Alt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse_Alt.class)
            .SetPower(2, CardRarity.SPECIAL)
            .SetSeries(ElricAlphonse.DATA.Series)
            .SetMaxCopies(1);

    public ElricAlphonse_Alt()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainOrbSlots(1);
        GameActions.Bottom.GainPlatedArmor(magicNumber);
        GameActions.Bottom.StackPower(new ElricAlphonse_AltPower(p, 1));
    }

    public static class ElricAlphonse_AltPower extends AnimatorPower
    {
        public ElricAlphonse_AltPower(AbstractCreature owner, int amount)
        {
            super(owner, ElricAlphonse_Alt.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Top.ChannelOrbs(Lightning::new, amount).AutoEvoke(false);
            flashWithoutSound();
        }
    }
}