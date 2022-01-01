package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.powers.special.SwirledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Venti extends PCLCard
{
    public static final PCLCardData DATA = Register(Venti.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public Venti()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Star(1, 0, 0);
        SetAffinity_Green(0,0,2);
        SetAffinity_Light(0, 0, 1);

        SetEthereal(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form + 1]);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                Initialize(0, 1, 2, 2);
                SetUpgrade(0, 0, 0, 0);
                upgradedDamage = true;
            }
            else {
                Initialize(0, 1, 3, 2);
                SetUpgrade(0, 0, 0, 0);
                SetHaste(true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        AbstractOrb orb = new Air();
        PCLActions.Bottom.ChannelOrb(orb);
        PCLActions.Bottom.StackPower(new VentiPower(player, 1));

        PCLActions.Bottom.Cycle(name, magicNumber).SetOptions(true, true, true).AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (card.type == CardType.SKILL)
                {
                    PCLActions.Bottom.TriggerOrbPassive(auxiliaryData.form == 1 && upgraded ? player.orbs.size() : 1).SetFilter(o -> Air.ORB_ID.equals(o.ID));
                }
                else {
                    PCLActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
                }
            }
        });

    }
    public static class VentiPower extends PCLPower implements OnOrbPassiveEffectSubscriber
    {
        public VentiPower(AbstractCreature owner, int amount)
        {
            super(owner, Venti.DATA);

            Initialize(amount);
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
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Air.ORB_ID.equals(orb.ID)) {
                for (AbstractMonster m : PCLGameUtilities.GetEnemies(true)) {
                    PCLActions.Delayed.StackPower(player, new SwirledPower(m, 1));
                }
                flash();
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Air.ORB_ID.equals(orb.ID)) {
                for (AbstractMonster m : PCLGameUtilities.GetEnemies(true)) {
                    PCLActions.Delayed.StackPower(player, new SwirledPower(m, 1));
                }
                flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            ReducePower(1);
        }
    }

}