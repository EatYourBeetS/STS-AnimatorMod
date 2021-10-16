package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Darkness_Adrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Darkness extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Darkness.class).SetPower(1, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new Darkness_Adrenaline(), false);
    }

    public Darkness()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Earth();
        SetAffinity_Steel();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new DarknessPower(p, magicNumber));
    }

    public static class DarknessPower extends AnimatorPower
    {
        public static final String POWER_ID = CreateFullID(DarknessPower.class);

        public DarknessPower(AbstractPlayer owner, int amount)
        {
            super(owner, Darkness.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
            {
                flash();
                GameActions.Top.ReducePower(this, 1);
                GameActions.Bottom.MakeCardInDrawPile(new Darkness_Adrenaline()).Repeat(amount).SetDestination(CardSelection.Top);
            }
        }
    }
}
