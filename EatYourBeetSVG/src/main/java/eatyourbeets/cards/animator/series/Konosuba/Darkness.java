package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.animator.special.Darkness_Adrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Darkness extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Darkness.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Darkness_Adrenaline(), false));

    public Darkness()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new PlatedArmorPower(p, magicNumber));
        GameActions.Bottom.StackPower(new DarknessPower(p, 1));
    }

    public static class DarknessPower extends AnimatorPower
    {
        public DarknessPower(AbstractPlayer owner, int amount)
        {
            super(owner, Darkness.DATA);

            Initialize(amount);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (amount > 0 && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
            {
                GameActions.Bottom.MakeCardInDrawPile(new Darkness_Adrenaline());
                if ((amount -= 1) <= 0)
                {
                    RemovePower();
                }

                this.flash();
            }
        }
    }
}