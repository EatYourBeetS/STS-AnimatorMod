package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnCooldownTriggeredSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class AlinaGray extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(AlinaGray.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public AlinaGray()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Blue(1, 1, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.EvokeOrb(secondaryValue).AddCallback(orbs -> {
            for (AbstractOrb o : orbs) {
                GameActions.Bottom.ChannelOrb(o);
            }
        });

        GameActions.Bottom.StackPower(new AlinaGrayPower(player, magicNumber));
    }

    public static class AlinaGrayPower extends AnimatorPower implements OnCooldownTriggeredSubscriber
    {
        public AlinaGrayPower(AbstractCreature owner, int amount)
        {
            super(owner, AlinaGray.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onCooldownTriggered.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onCooldownTriggered.Unsubscribe(this);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }

        @Override
        public boolean OnCooldownTriggered(AbstractCard card, EYBCardCooldown cooldown) {
            if (cooldown.cardConstructor != null) {
                AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
                if (enemy != null && cooldown.card.cooldownValue == 0 && CombatStats.TryActivateLimited(AlinaGray.DATA.ID)) {
                    for (AbstractPower po : enemy.powers) {
                        for (PowerHelper commonBuffHelper : GameUtilities.GetCommonBuffs()) {
                            if (commonBuffHelper.ID.equals(po.ID)) {
                                int powAmount = Math.min(2,GameUtilities.GetPowerAmount(enemy, po.ID));
                                GameActions.Bottom.ReducePower(po, powAmount);
                                GameActions.Bottom.StackPower(TargetHelper.Player(), commonBuffHelper, powAmount);
                                return false;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }
    }
}