package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Laby extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Laby.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 40, 1);
        SetUpgrade(0,0,25);

        SetAffinity_Mind(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, magicNumber));
        GameActions.Bottom.StackPower(new LabyPower(p, secondaryValue));
    }

    public static class LabyPower extends AnimatorPower
    {
        public LabyPower(AbstractCreature owner, int amount)
        {
            super(owner, Laby.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.ApplyConstricted(TargetHelper.AllCharacters(), amount)
            .ShowEffect(false, true);
            SFX.Play(SFX.POWER_CONSTRICTED);
            this.flashWithoutSound();
        }
    }
}