package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 4, 0);
        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (CheckAffinity(Affinity.Orange) && CheckAffinity(Affinity.Blue)) {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddStrength(-magicNumber);
            }
        }

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (CheckAffinity(Affinity.Orange) && CheckAffinity(Affinity.Blue)) {
            for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.ReduceStrength(enemy, magicNumber, true);
            }
        }

        GameActions.Bottom.StackPower(new KagariPower(p, 1));
    }

    public static class KagariPower extends AnimatorPower implements OnChannelOrbSubscriber
    {

        public KagariPower(AbstractPlayer owner, int amount)
        {
            super(owner, Kagari.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ResetAmount();
        }

        @Override
        public void OnChannelOrb(AbstractOrb orb) {
            if (Earth.ORB_ID.equals(orb.ID) && owner.isPlayer && amount > 0) {
                GameActions.Bottom.GainWillpower(amount, player.stance.ID.equals(WillpowerStance.STANCE_ID));
                this.amount -= 1;
                updateDescription();
                flash();
            }
        }
    }
}