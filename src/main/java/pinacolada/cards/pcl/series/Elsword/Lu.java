package pinacolada.cards.pcl.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.stances.NeutralStance;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_ApplyToEnemies;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Lu extends PCLCard
{
    public static final PCLCardData DATA = Register(Lu.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ice)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Ciel(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Lu()
    {
        super(DATA);

        Initialize(2, 0, 3);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAttackTarget(PCLCardTarget.AoE);
        SetMultiDamage(true);
        upgradedDamage = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (isMultiDamage)
        {
            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect((enemy, __) -> PCLGameEffects.List.Add(VFX.Claw(enemy.hb, Color.VIOLET, Color.WHITE))));
        }
        else
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
            .SetDamageEffect(enemy -> PCLGameEffects.List.Add(VFX.Claw(enemy.hb, Color.VIOLET, Color.WHITE)).duration));
        }

        if (damage >= 20)
        {
            PCLActions.Bottom.Add(new ShakeScreenAction(0.8f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        PCLActions.Bottom.ChannelOrb(new Frost());
        PCLActions.Bottom.ChannelOrb(new Dark());

        PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
                .AddCallback(info, (info2, stance) ->
                {
                    if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID))
                    {
                        choices.Initialize(this, true);
                        choices.AddEffect(new GenericEffect_Ciel(magicNumber));
                        choices.AddEffect(new GenericEffect_ApplyToEnemies(PCLPowerHelper.Freezing, magicNumber));
                        choices.Select(1, m);
                    }
                });
    }

    protected static class GenericEffect_Ciel extends GenericEffect
    {
        public GenericEffect_Ciel(int amount)
        {
            this.amount = amount;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(Lu.DATA.Strings.EXTENDED_DESCRIPTION[0]);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.ModifyAllCopies(Ciel.DATA.ID)
                    .AddCallback(c ->
                    {
                        PCLGameUtilities.IncreaseBlock(c, amount, false);
                        c.flash();
                    });
        }
    }
}