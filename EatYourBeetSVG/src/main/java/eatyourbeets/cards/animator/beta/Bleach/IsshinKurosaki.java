package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.misc.GenericEffects.GenericEffect_ChannelOrb;
import eatyourbeets.misc.GenericEffects.GenericEffect_GainStat;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.PlayerAttribute;

public class IsshinKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsshinKurosaki.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(0, 6, 2, 2);
        SetUpgrade(0, 1, 1);
        SetMartialArtist();

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_ChannelOrb(magicNumber, new Fire()));
            choices.AddEffect(new GenericEffect_GainStat(secondaryValue, PlayerAttribute.Force));
        }

        choices.Select(secondaryValue, m);

        int force = GameUtilities.GetPowerAmount(AbstractDungeon.player, ForcePower.POWER_ID);
        int agility = GameUtilities.GetPowerAmount(AbstractDungeon.player, AgilityPower.POWER_ID);
        int gainAmount = force + agility;

        GameActions.Bottom.StackPower(new IsshinKurosakiPower(player, 1));
    }

    public static class IsshinKurosakiPower extends AnimatorPower implements OnStartOfTurnPostDrawSubscriber
    {
        public IsshinKurosakiPower(AbstractCreature owner, int amount)
        {
            super(owner, IsshinKurosaki.DATA);

            this.amount = amount;
            this.type = PowerType.BUFF;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            BoostAllFireOrbs();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            for (AbstractOrb orb : player.orbs)
            {
                if (Fire.ORB_ID.equals(orb.ID))
                {
                    orb.passiveAmount -= amount;
                    orb.evokeAmount -= amount;
                }
            }
        }

        @Override
        public void OnStartOfTurnPostDraw()
        {
            RemovePower();
        }

        private void BoostAllFireOrbs()
        {
            for (AbstractOrb orb : player.orbs)
            {
                if (Fire.ORB_ID.equals(orb.ID))
                {
                    BoostOrb(orb);
                }
            }
        }

        private void BoostOrb(AbstractOrb orb)
        {
            orb.passiveAmount = orb.passiveAmount + amount;
            orb.evokeAmount = amount;
        }

        @Override
        public void updateDescription()
        {
            BoostAllFireOrbs();
            description = FormatDescription(0, amount);
        }
    }
}