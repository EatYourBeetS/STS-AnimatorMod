package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import static pinacolada.resources.GR.Enums.CardTags.PCL_EXHAUST;

public class Albedo extends PCLCard
{
    public static final PCLCardData DATA = Register(Albedo.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Albedo()
    {
        super(DATA);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new PlatedArmorPower(p, secondaryValue));
        if (upgraded) {
            PCLActions.Bottom.GainArtifact(1);
        }
        PCLActions.Bottom.StackPower(new AlbedoPower(p, magicNumber));
    }

    public static class AlbedoPower extends PCLClickablePower implements OnOrbPassiveEffectSubscriber
    {

        public AlbedoPower(AbstractCreature owner, int amount)
        {
            super(owner, Albedo.DATA, PowerTriggerConditionType.Affinity, -1, null, null, PCLAffinity.Dark);

            Initialize(amount);
            this.triggerCondition.SetOneUsePerPower(true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true));
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.SelectFromHand(name,1,false)
                    .SetFilter(c -> c.type == CardType.ATTACK)
                    .AddCallback(cards -> {
                for (AbstractCard card: cards) {
                    PCLGameUtilities.IncreaseDamage(card, cost,false);
                    PCLActions.Bottom.ModifyTag(card, PCL_EXHAUST, true);
                }
            });
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Dark.ORB_ID.equals(orb.ID)) {
                PCLActions.Bottom.AddAffinity(PCLAffinity.Dark, amount);
            }
        }
    }
}