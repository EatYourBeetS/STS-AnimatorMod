package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2)
            .PostInitialize(data ->
            {
                for (EYBCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), false);
                }
            });
    private static final int POWER_ENERGY_COST = 2;
    private static final int CHOICES = 3;

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetDelayed(false);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    };


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EvePower(p, magicNumber));
    }

    public static class EvePower extends AnimatorClickablePower
    {
        public EvePower(AbstractCreature owner, int amount)
        {
            super(owner, Eve.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                    .AddCallback(c -> {if (c.size() > 0) {GameActions.Bottom.PlayCard(c.get(0), m);}}));
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (CombatStats.Affinities.IsSynergizing(usedCard))
            {
                final int damage = CombatStats.Affinities.GetHandAffinityLevel(Affinity.General, usedCard);
                if (damage > 0)
                {
                    //GameEffects.Queue.BorderFlash(Color.SKY);
                    for (int i = 0; i < amount; i++)
                    {
                        GameActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                        .SetOptions(true, false)
                        .SetDamageEffect(enemy ->
                        {
                            SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f);
                            GameEffects.List.Add(VFX.SmallLaser(owner.hb, enemy.hb, Color.CYAN));
                            return 0f;
                        });
                    }

                    this.flash();
                    this.updateDescription();
                }
            }
        }
    }
}