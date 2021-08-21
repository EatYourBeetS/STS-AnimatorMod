package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
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
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 2;

    static
    {
        for (EYBCardData data : AffinityToken.GetCards())
        {
            DATA.AddPreview(data.CreateNewInstance(), false);
        }
    }

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Blue(2);
        SetAffinity_Light(1, 1, 0);
        SetAffinity_Dark(1, 1, 0);

        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new EvePower(p, magicNumber));
    }

    public static class EvePower extends AnimatorClickablePower
    {
        public EvePower(AbstractCreature owner, int amount)
        {
            super(owner, Eve.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            this.amount = amount;
            this.triggerCondition.SetOneUsePerPower(true);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.Add(AffinityToken.SelectTokenAction(name, 1, 3)
            .AddCallback(cards ->
            {
                if (cards != null && cards.size() > 0)
                {
                    for (AbstractCard c : cards)
                    {
                        GameActions.Bottom.PlayCard(c, null);
                        //c.applyPowers();
                        //c.use(player, null);
                        //CombatStats.Affinities.SetLastCardPlayed(c);
                    }
                }
            }));
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (CombatStats.Affinities.IsSynergizing(usedCard))
            {
                int damage = CombatStats.Affinities.GetHandAffinityLevel(Affinity.General, usedCard);
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