package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import eatyourbeets.cards.animator.beta.special.BlazingHeat;
import eatyourbeets.cards.animator.series.TenseiSlime.Shizu;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class Shizu_Ifrit extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu_Ifrit.class)
            .SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Shizu.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new BlazingHeat(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Shizu_Ifrit()
    {
        super(DATA);

        Initialize(0, 0, 40, 2);
        SetUpgrade(0, 0, 10);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetAffinityRequirement(Affinity.Red, 10);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final ScreenOnFireEffect effect = new ScreenOnFireEffect();
        effect.duration = effect.startingDuration = 1.5f; // Changed from 3f
        GameActions.Bottom.VFX(effect, 0.2f);
        GameActions.Bottom.ApplyBurning(TargetHelper.AllCharacters(),secondaryValue);
        GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(BurningPower.POWER_ID, magicNumber));


        if (CheckAffinity(Affinity.Red) && CombatStats.TryActivateLimited(cardID)) {
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
            return JUtils.Format(Shizu_Ifrit.DATA.Strings.EXTENDED_DESCRIPTION[0]);
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            GameActions.Bottom.StackPower(new DemonFormPower(p, 2));
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
            return JUtils.Format(Shizu_Ifrit.DATA.Strings.EXTENDED_DESCRIPTION[1]);
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
        }
    }
}