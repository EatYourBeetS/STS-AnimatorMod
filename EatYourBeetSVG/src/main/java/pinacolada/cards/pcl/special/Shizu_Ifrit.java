package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.series.TenseiSlime.Shizu;
import pinacolada.misc.GenericEffects.GenericEffect;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Shizu_Ifrit extends PCLCard
{
    public static final PCLCardData DATA = Register(Shizu_Ifrit.class)
            .SetSkill(3, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeries(Shizu.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new BlazingHeat(), false));
    public static final int AMOUNT = 2;
    public static final int SELF_BURNING = 4;
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Shizu_Ifrit()
    {
        super(DATA);

        Initialize(0, 0, 40, 11);
        SetUpgrade(0, 0, 0, 4);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(PCLAffinity.Dark, 6);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyBurning(TargetHelper.Player(),SELF_BURNING);
        PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(),secondaryValue);
        PCLActions.Bottom.Callback(() -> PCLTriggerablePower.AddEffectBonus(BurningPower.POWER_ID, magicNumber));


        if (info.CanActivateLimited && TrySpendAffinity(PCLAffinity.Dark) && CombatStats.TryActivateLimited(cardID)) {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_DemonForm());
                choices.AddEffect(new GenericEffect_BlazingHeat());
            }
            choices.Select(1, m);
        }
    }

    protected static class GenericEffect_DemonForm extends GenericEffect
    {
        public GenericEffect_DemonForm()
        {
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(Shizu_Ifrit.DATA.Strings.EXTENDED_DESCRIPTION[0]);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.StackPower(new DemonFormPower(p, AMOUNT));
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