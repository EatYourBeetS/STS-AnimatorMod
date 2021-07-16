package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Keqing extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, OnEvokeOrbSubscriber
{
    public static final EYBCardData DATA = Register(Keqing.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage();

    public Keqing()
    {
        super(DATA);

        Initialize(2, 0, 4, 4);
        SetUpgrade(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(2, 0, 1);

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
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CombatStats.onEvokeOrb.Subscribe(this);
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
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

                if (cost > 0)
                {
                    this.modifyCostForCombat(-1);
                }
                GameActions.Bottom.ModifyAllInstances(uuid)
                        .AddCallback(c ->
                        {
                            if (!c.hasTag(HASTE))
                            {
                                c.tags.add(HASTE);
                            }
                        });


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
}