package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.card.ChooseAndUpgradeEffect;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Rorona extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rorona.class).SetPower(3, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Atelier);

    public Rorona()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(2, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new RoronaPower(p, this.magicNumber));
    }

    public static class RoronaPower extends AnimatorPower implements OnBattleEndSubscriber
    {
        public RoronaPower(AbstractPlayer owner, int amount)
        {
            super(owner, Rorona.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            CombatStats.onBattleEnd.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onBattleEnd.Unsubscribe(this);
        }

        @Override
        public void OnBattleEnd()
        {
            GameEffects.Queue.Add(new ChooseAndUpgradeEffect(c -> {
                return c.rarity.equals(AbstractCard.CardRarity.BASIC) || c.rarity.equals(AbstractCard.CardRarity.COMMON);
            }, false));
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}