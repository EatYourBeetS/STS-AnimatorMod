package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiKureto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiKureto.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public HiiragiKureto()
    {
        super(DATA);

        Initialize(7, 0, 3);
        SetUpgrade(0, 0);

        SetAffinity_Red(1, 1, 2);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY).AddCallback(e -> {
                if (e.lastDamageTaken > 0) {
                    GameActions.Bottom.ExhaustFromHand(name,1,true).AddCallback(
                            cards -> {
                                if (cards.size() > 0) {
                                    GameActions.Bottom.StackPower(new HiiragiKuretoPower(player, 1));
                                }
                            }
                    );
                }
            });
        }
    }

    public class HiiragiKuretoPower extends AnimatorPower
    {
        public HiiragiKuretoPower(AbstractCreature owner, int amount)
        {
            super(owner, HiiragiKureto.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (card.type.equals(CardType.ATTACK)) {
                if (GameUtilities.GetOrbCount(Lightning.ORB_ID) == 0) {
                    GameActions.Top.ChannelOrb(new Lightning());
                }
                GameActions.Bottom.TriggerOrbPassive(player.orbs.size())
                        .SetFilter(o -> Lightning.ORB_ID.equals(o.ID))
                        .SetSequential(true);
                this.amount -= 1;
                if (this.amount <= 0) {
                    RemovePower();
                }
                this.flash();
            }
        }
    }
}