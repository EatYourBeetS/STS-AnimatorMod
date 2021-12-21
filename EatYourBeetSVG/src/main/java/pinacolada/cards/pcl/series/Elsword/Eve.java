package pinacolada.cards.pcl.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Eve_Drone;
import pinacolada.cards.pcl.special.OrbCore;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.util.UUID;

public class Eve extends PCLCard
{
    public static final PCLCardData DATA = Register(Eve.class)
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
    public static final PCLCardPreview DRONE_PREVIEW_ALT = new PCLCardPreview(new Eve_Drone(1), false);
    private static final int POWER_ENERGY_COST = 2;
    private static final int CHOICES = 3;

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
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
    public PCLCardPreview GetCardPreview()
    {
        return auxiliaryData.form == 1 && cardData.previews.GetIndex() == 0 ? DRONE_PREVIEW_ALT : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(c -> {if (c.size() > 0) {
                    PCLActions.Bottom.PlayCard(c.get(0), m);}}));
        if (secondaryValue > 0)
        {
            PCLActions.Bottom.GainOrbSlots(secondaryValue);
        }
        PCLActions.Bottom.StackPower(new EvePower(p, magicNumber, auxiliaryData.form));
    }

    public static class EvePower extends PCLClickablePower
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

            PCLActions.Bottom.SelectFromPile(name, 1, choice)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        PCLActions.Bottom.MakeCardInDrawPile(cards.get(0));
                        PCLActions.Bottom.MakeCardInHand(cards.get(0));
                        PCLActions.Bottom.Add(new RefreshHandLayout());
                    });
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (PCLCombatStats.MatchingSystem.IsSynergizing(usedCard))
            {
                PCLAffinity lowestAffinity = PCLJUtils.FindMin(PCLAffinity.Extended(), af -> PCLCombatStats.MatchingSystem.GetAffinityLevel((PCLAffinity) af,true));
                final int damage = PCLCombatStats.MatchingSystem.GetAffinityLevel(lowestAffinity,true);
                if (damage > 0)
                {
                    //GameEffects.Queue.BorderFlash(Color.SKY);
                    for (int i = 0; i < amount; i++)
                    {
                        PCLActions.Bottom.DealDamageToRandomEnemy(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                        .SetOptions(true, false)
                        .SetDamageEffect(enemy ->
                        {
                            SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f);
                            PCLGameEffects.List.Add(VFX.SmallLaser(owner.hb, enemy.hb, Color.CYAN));
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