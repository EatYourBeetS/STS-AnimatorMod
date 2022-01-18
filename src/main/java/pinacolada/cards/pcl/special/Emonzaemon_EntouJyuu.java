package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Katanagatari.Emonzaemon;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Emonzaemon_EntouJyuu extends PCLCard
{
    public static final PCLCardData DATA = Register(Emonzaemon_EntouJyuu.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetSeries(Emonzaemon.DATA.Series);
    public static final int BONUS_DAMAGE = 25;

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

    public static class Emonzaemon_EntouJyuuPower extends PCLPower
    {
        public Emonzaemon_EntouJyuuPower(AbstractCreature owner, int amount)
        {
            super(owner, Emonzaemon_EntouJyuu.DATA);

            Initialize(amount);
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card)
        {
            return damage + (type == DamageInfo.DamageType.NORMAL && (ThrowingKnife.DATA.ID.equals(card.cardID) || PCLGameUtilities.HasGreenAffinity(card)) ? amount : 0) / 100f;
        }
    }
}