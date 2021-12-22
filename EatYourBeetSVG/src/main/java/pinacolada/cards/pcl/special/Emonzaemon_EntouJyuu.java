package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Katanagatari.Emonzaemon;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class Emonzaemon_EntouJyuu extends PCLCard
{
    public static final PCLCardData DATA = Register(Emonzaemon_EntouJyuu.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetSeries(Emonzaemon.DATA.Series);
    public static final int BONUS_DAMAGE = 3;

    public Emonzaemon_EntouJyuu()
    {
        super(DATA);

        Initialize(0, 0, 1, BONUS_DAMAGE);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryDexterity, magicNumber);
        PCLActions.Bottom.StackPower(new Emonzaemon_EntouJyuuPower(p, secondaryValue));
    }

    public class Emonzaemon_EntouJyuuPower extends PCLPower
    {
        private int attacks;

        public Emonzaemon_EntouJyuuPower(AbstractCreature owner, int amount)
        {
            super(owner, Emonzaemon_EntouJyuu.DATA);

            this.attacks = 2;

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            SetEnabled(true);
            attacks = 2;
        }

        @Override
        public void onAfterUseCard(AbstractCard card, UseCardAction action)
        {
            super.onAfterUseCard(card, action);

            if (attacks > 0 && card.type == CardType.ATTACK)
            {
                this.flashWithoutSound();
                attacks -= 1;

                if (attacks <= 0)
                {
                    SetEnabled(false);
                }
            }
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type)
        {
            return damage + (type == DamageInfo.DamageType.NORMAL ? amount : 0);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(attacks, Settings.CREAM_COLOR, c.a);
        }
    }
}