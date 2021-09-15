package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MotokoKusanagi extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final int GOLD_THRESHOLD = 150;
    public static final int BASE_RICOCHET = 3;

    public static final EYBCardData DATA = Register(MotokoKusanagi.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GhostInTheShell);

    public MotokoKusanagi()
    {
        super(DATA);

        Initialize(3, 0, 2, 3);
        SetUpgrade(2, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
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
    public void triggerWhenCreated(boolean startOfBattle)
    {
        GameUtilities.ModifySecondaryValue(this, Math.max(1, BASE_RICOCHET - Math.floorDiv(player.gold, GOLD_THRESHOLD)), true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        GameActions.Bottom.GainBlur(magicNumber);

        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
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
                GameUtilities.ModifySecondaryValue(this, Math.max(1, BASE_RICOCHET - Math.floorDiv(player.gold, GOLD_THRESHOLD)), true);
                GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                        .ShowEffect(true, false);

                CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            }
            else
            {
                GameUtilities.ModifySecondaryValue(this, secondaryValue - 1, true);
            }
        }
        else
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}