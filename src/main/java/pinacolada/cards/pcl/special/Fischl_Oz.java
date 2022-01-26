package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnMatchBonusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class Fischl_Oz extends PCLCard
{
    public static final PCLCardData DATA = Register(Fischl_Oz.class).SetSkill(2, CardRarity.SPECIAL, PCLCardTarget.Random).SetSeries(CardSeries.GenshinImpact);

    public Fischl_Oz()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyElectrified(TargetHelper.RandomEnemy(), magicNumber);
        PCLActions.Bottom.StackPower(new OzPower(p, this.secondaryValue));
    }

    public static class OzPower extends PCLPower implements OnMatchBonusSubscriber
    {
        public OzPower(AbstractPlayer owner, int amount)
        {
            super(owner, Fischl_Oz.DATA);

            Initialize(amount);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onMatchBonus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onMatchBonus.Unsubscribe(this);
        }

        @Override
        public void OnMatchBonus(AbstractCard card, PCLAffinity affinity)
        {
            if (amount > 0 && PCLAffinity.Blue.equals(affinity))
            {
                PCLActions.Bottom.ChannelOrb(rng.randomBoolean(0.5f) ? new Dark() : new Lightning());
                ReducePower(1);
                flash();
            }
        }
    }
}