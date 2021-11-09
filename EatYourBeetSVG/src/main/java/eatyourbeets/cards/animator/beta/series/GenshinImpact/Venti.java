package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SwirledPower;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Venti extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(2).SetSeriesFromClassPackage();

    public Venti()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Star(2, 0, 0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AbstractOrb orb = new Air();
        GameActions.Bottom.ChannelOrb(orb);
        GameActions.Bottom.StackPower(new VentiPower(player, 1));

        GameActions.Bottom.Cycle(name, magicNumber).SetOptions(true, true, true).AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (card.type == CardType.SKILL)
                {
                    GameActions.Bottom.TriggerOrbPassive(1).SetFilter(o -> Air.ORB_ID.equals(o.ID));
                }
                else {
                    GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
                }
            }
        });

    }
    public static class VentiPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber
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

            CombatStats.onOrbPassiveEffect.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onOrbPassiveEffect.Unsubscribe(this);
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Air.ORB_ID.equals(orb.ID)) {
                for (AbstractMonster m : GameUtilities.GetEnemies(true)) {
                    GameActions.Delayed.StackPower(player, new SwirledPower(m, 1));
                }
                flash();
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Air.ORB_ID.equals(orb.ID)) {
                for (AbstractMonster m : GameUtilities.GetEnemies(true)) {
                    GameActions.Delayed.StackPower(player, new SwirledPower(m, 1));
                }
                flash();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            RemovePower(GameActions.Delayed);
        }
    }

}