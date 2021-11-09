package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
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
                data.AddPreview(new Eve_Drone(0), false);
                for (OrbCore core : OrbCore.GetAllCores())
                {
                    data.AddPreview(core, false);
                }
            });
    public static final EYBCardPreview DRONE_PREVIEW_ALT = new EYBCardPreview(new Eve_Drone(1), false);
    private static final int POWER_ENERGY_COST = 2;
    private static final int CHOICES = 3;

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0,0,1,1);

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
    public EYBCardPreview GetCardPreview()
    {
        return auxiliaryData.form == 1 && cardData.previews.GetIndex() == 0 ? DRONE_PREVIEW_ALT : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(c -> {if (c.size() > 0) {GameActions.Bottom.PlayCard(c.get(0), m);}}));
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(secondaryValue);
        }
        GameActions.Bottom.StackPower(new EvePower(p, magicNumber, auxiliaryData.form));
    }

    public static class EvePower extends AnimatorClickablePower
    {
        private static int[] availableChoices = new int[] {0, 0};
        private static UUID battleID;

        public EvePower(AbstractCreature owner, int amount, int option)
        {
            super(owner, Eve.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);
            if (CombatStats.BattleID != battleID)
            {
                battleID = CombatStats.BattleID;
                availableChoices = new int[] {0, 0};
            }
            availableChoices[option] = 1;

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (availableChoices[0] == 1) {
                choice.addToTop(new Eve_Drone(0));
            }
            if (availableChoices[1] == 1) {
                choice.addToTop(new Eve_Drone(1));
            }

            GameActions.Bottom.SelectFromPile(name, 1, choice)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        GameActions.Bottom.MakeCardInDrawPile(cards.get(0));
                        GameActions.Bottom.MakeCardInHand(cards.get(0));
                        GameActions.Bottom.Add(new RefreshHandLayout());
                    });
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (CombatStats.Affinities.IsSynergizing(usedCard))
            {
                Affinity lowestAffinity = JUtils.FindMin(Affinity.Extended(), af -> CombatStats.Affinities.GetAffinityLevel((Affinity) af,true));
                final int damage = CombatStats.Affinities.GetAffinityLevel(lowestAffinity,true);
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
            return FormatDescription(0, availableChoices[0] == 1 && availableChoices[1] == 1 ? DATA.Strings.EXTENDED_DESCRIPTION[1] : "");
        }
    }
}