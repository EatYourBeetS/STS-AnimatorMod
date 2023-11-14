package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.interfaces.subscribers.OnAfterCardExhaustedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class FeridBathory extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FeridBathory.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final int HP_STEAL = 2;

    public FeridBathory()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2);
        SetAffinity_Green(1);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetRetainOnce(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return new ColoredString(HP_STEAL, Colors.Cream(1));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainCorruption(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.DelayedTop.StackPower(p, new FeridBathoryPower(m, p, HP_STEAL));
    }

    public static class FeridBathoryPower extends AnimatorPower implements OnAfterCardExhaustedSubscriber
    {
        public FeridBathoryPower(AbstractCreature owner, AbstractCreature source, int amount)
        {
            super(owner, source, FeridBathory.DATA);

            Initialize(amount, PowerType.DEBUFF, false);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAfterCardExhausted.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAfterCardExhausted.Unsubscribe(this);
        }

        @Override
        public void OnAfterCardExhausted(AbstractCard card)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(enemy -> GameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY)).duration);
            GameActions.Bottom.GainTemporaryHP(amount);
            flashWithoutSound();
        }
    }
}