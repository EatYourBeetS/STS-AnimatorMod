package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Eve_Drone;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.HashSet;
import java.util.UUID;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data ->
            {
                for (OrbCore core : OrbCore.GetAllCores())
                {
                    data.AddPreview(core, false);
                }
            });
    public static final EYBCardPreview DRONE_PREVIEW = new EYBCardPreview(new Eve_Drone(), false);
    private static final int POWER_ENERGY_COST = 2;
    private static final int CHOICES = 3;

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0,0,0,1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Silver(2);

        SetDelayed(true);
        SetRetainOnce(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            LoadImage("_Light");
        }
        else {
            LoadImage(null);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription(auxiliaryData.form == 1 ? cardData.Strings.EXTENDED_DESCRIPTION[2] : cardData.Strings.EXTENDED_DESCRIPTION[1]);
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return auxiliaryData.form == 1 ? DRONE_PREVIEW : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(secondaryValue);
        }
        GameActions.Bottom.StackPower(new EvePower(p, magicNumber, auxiliaryData.form));
    }

    public static class EvePower extends AnimatorClickablePower
    {
        private static final CardEffectChoice choices = new CardEffectChoice();
        private static final HashSet<Integer> availableChoices = new HashSet<>();
        private static UUID battleID;

        public EvePower(AbstractCreature owner, int amount, int option)
        {
            super(owner, Eve.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);
            if (CombatStats.BattleID != battleID)
            {
                battleID = CombatStats.BattleID;
                availableChoices.clear();
            }
            availableChoices.add(option);

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            choices.Initialize(new Eve(), true);
            if (availableChoices.contains(1)) {
                choices.AddEffect( JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[2],amount), (c, p, mo) -> {
                    GameActions.Bottom.MakeCardInDrawPile(new Eve_Drone());
                    GameActions.Bottom.MakeCardInHand(new Eve_Drone());
                });
            }
            if (availableChoices.contains(0)) {
                choices.AddEffect( JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[1],amount), (c, p, mo) -> {
                    GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                        .AddCallback(ca -> {if (ca.size() > 0) {GameActions.Bottom.PlayCard(ca.get(0), m);}}));
                });
            }
            choices.Select(1,null);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (CombatStats.Affinities.IsSynergizing(usedCard))
            {
                Affinity highestAffinity = JUtils.FindMax(Affinity.Basic(), af -> CombatStats.Affinities.GetAffinityLevel((Affinity) af,true));
                final int damage = CombatStats.Affinities.GetAffinityLevel(highestAffinity,true);
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

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, availableChoices.size() > 1 ? DATA.Strings.EXTENDED_DESCRIPTION[5] : availableChoices.contains(1) ? DATA.Strings.EXTENDED_DESCRIPTION[4] : DATA.Strings.EXTENDED_DESCRIPTION[3]);
        }
    }
}