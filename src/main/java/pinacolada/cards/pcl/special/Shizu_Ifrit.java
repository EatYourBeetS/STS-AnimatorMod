package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect;
import pinacolada.cards.pcl.series.TenseiSlime.Shizu;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.special.BurningWeaponPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Shizu_Ifrit extends PCLCard
{
    public static final PCLCardData DATA = Register(Shizu_Ifrit.class)
            .SetSkill(3, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeries(Shizu.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new BlazingHeat(), false));
    public static final int AMOUNT = 2;
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Shizu_Ifrit()
    {
        super(DATA);

        Initialize(0, 10, 40, 10);
        SetUpgrade(0, 0, 10, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Dark, 6);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyBurning(TargetHelper.AllCharacters(),secondaryValue);
        PCLActions.Bottom.AddPowerEffectEnemyBonus(BurningPower.POWER_ID, magicNumber);

        if (info.CanActivateLimited && TrySpendAffinity(PCLAffinity.Dark) && CombatStats.TryActivateLimited(cardID)) {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_BurningWeapon(secondaryValue));
                choices.AddEffect(new GenericEffect_BlazingHeat());
            }
            choices.Select(1, m);
        }
    }

    protected static class GenericEffect_BurningWeapon extends GenericEffect
    {
        public GenericEffect_BurningWeapon(int amount)
        {
            this.amount = amount;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(Shizu_Ifrit.DATA.Strings.EXTENDED_DESCRIPTION[0], amount);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.StackPower(new BurningWeaponPower(p, amount));
        }
    }

    protected static class GenericEffect_BlazingHeat extends GenericEffect
    {
        public GenericEffect_BlazingHeat()
        {
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(Shizu_Ifrit.DATA.Strings.EXTENDED_DESCRIPTION[1]);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
        }
    }
}