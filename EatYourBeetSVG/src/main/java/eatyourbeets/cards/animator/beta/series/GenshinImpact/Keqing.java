package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.*;

public class Keqing extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, OnEvokeOrbSubscriber, OnApplyPowerSubscriber
{
    public static final EYBCardData DATA = Register(Keqing.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage();

    public Keqing()
    {
        super(DATA);

        Initialize(2, 0, 4, 4);
        SetUpgrade(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(2, 0, 4);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(3);
    }

    @Override
    public ColoredString GetSecondaryValueString()
    {
        if (this.isSecondaryValueModified)
        {
            if (this.secondaryValue > 0)
            {
                return new ColoredString(this.secondaryValue, Settings.GREEN_TEXT_COLOR.cpy().lerp(Settings.CREAM_COLOR, 0.5f), this.transparency);
            }
            else
            {
                return new ColoredString(this.secondaryValue, Settings.GREEN_TEXT_COLOR, this.transparency);
            }
        }
        else
        {
            return new ColoredString(this.secondaryValue, Settings.CREAM_COLOR, this.transparency);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()).duration);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER);

        AbstractCard card = null;
        RandomizedList<AbstractCard> possible = new RandomizedList<>();
        for (AbstractCard c : player.discardPile.group)
        {
            if (c.costForTurn >= 0)
            {
                possible.Add(c);
            }
        }
        if (possible.Size() > 0)
        {
            card = possible.Retrieve(rng);
        }

        ModifyCard(this);
        ModifyCard(card);

        CombatStats.onEvokeOrb.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnEvokeOrb(AbstractOrb orb)
    {
        if (Lightning.ORB_ID.equals(orb.ID))
        {
            this.reduceTurns();
        }

    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.ID.equals(BlurPower.POWER_ID) && target == player) {
            this.reduceTurns();
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        this.reduceTurns();
    }

    private void reduceTurns()
    {
        if (player.exhaustPile.contains(this))
        {
            if (secondaryValue <= 1)
            {
                GameUtilities.ModifySecondaryValue(this, magicNumber, true);
                GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                        .ShowEffect(true, false);
                CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
                CombatStats.onEvokeOrb.Unsubscribe(this);
            }
            else
            {
                GameUtilities.ModifySecondaryValue(this, secondaryValue - 1, true);
            }
        }
        else
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onEvokeOrb.Unsubscribe(this);
        }
    }

    private void ModifyCard(AbstractCard card){
        if (card != null) {
            if (card.cost > 0)
            {
                GameActions.Bottom.Motivate(card, 1);
            }
            GameActions.Bottom.ModifyTag(card, HASTE, true);
        }

    }
}