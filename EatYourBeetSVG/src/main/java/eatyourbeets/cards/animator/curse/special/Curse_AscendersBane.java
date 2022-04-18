package eatyourbeets.cards.animator.curse.special;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.PurgeAnywhere;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.listeners.OnRemovedFromDeckListener;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorAscensionManager;
import eatyourbeets.utilities.*;

public class Curse_AscendersBane extends AnimatorCard implements OnRemovedFromDeckListener, CustomSavable<Boolean>
{
    public static final int ASCENSION_THRESHOLD_ACT1 = AnimatorAscensionManager.ASCENSION_22_ASCENDERS_BANE;
    public static final int ASCENSION_THRESHOLD_ACT5 = 17;
    public static final EYBCardData DATA = Register(Curse_AscendersBane.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, true);

    public int EffectLevel;
    public ColoredString bottomText;

    public static boolean CheckAscension()
    {
        return (GameUtilities.GetActualAscensionLevel() >= ASCENSION_THRESHOLD_ACT1);
    }

    public static boolean CheckAct5()
    {
        return (GameUtilities.GetAscensionLevel() >= ASCENSION_THRESHOLD_ACT5
        && (GR.Common.Dungeon.IsUnnamedReign() || (player != null && GameUtilities.GetRelic(UnnamedReignRelic.class) != null)));
    }

    public Curse_AscendersBane()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
        SetEthereal(true);
    }

    @Override
    public void OnRemovedFromDeck()
    {
        GameEffects.TopLevelQueue.ShowAndObtain(this);
    }

    @Override
    public Boolean onSave()
    {
        return true;
    }

    @Override
    public void onLoad(Boolean value)
    {
        if (CheckAct5())
        {
            SetEffectLevel(2);
        }
        else if (CheckAscension())
        {
            SetEffectLevel(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!dontTriggerOnUseCard)
        {
            GameActions.Bottom.TakeDamage(magicNumber, AttackEffects.DARK)
            .SetSoundPitch(1.3f, 1.4f);
        }
        else if (EffectLevel >= 1)
        {
            GameActions.Bottom.SelectFromPile(name, 1, player.drawPile)
            .SetOptions(CardSelection.Top, false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    final PurgeAnywhere action = GameActions.Top.Purge(c).ShowEffect(true)
                    .SetTargetPosition(new Vector2(Settings.WIDTH * 0.4f, Settings.HEIGHT * 0.5f));

                    if (EffectLevel >= 2)
                    {
                        action.AddCallback(c, (card, purged) ->
                        {
                            if (purged && GameUtilities.IsHindrance(card))
                            {
                                GameActions.Bottom.MoveCard(this, player.drawPile)
                                .ShowEffect(true, true)
                                .SetDestination(CardSelection.Top);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        final Curse_AscendersBane copy = (Curse_AscendersBane) super.makeCopy();

        if (CheckAct5())
        {
            copy.SetEffectLevel(2);
        }
        else if (CheckAscension())
        {
            copy.SetEffectLevel(1);
        }

        return copy;
    }

    @Override
    public ColoredString GetBottomText()
    {
        return bottomText;
    }

    public void SetEffectLevel(int effectLevel)
    {
        EffectLevel = effectLevel;

        if (effectLevel <= 0)
        {
            timesUpgraded = 0;
            cardText.OverrideDescription(null, true);
            bottomText = null;
            cost = costForTurn = UNPLAYABLE_COST;
            SetEndOfTurnPlay(false);
            SetEthereal(true);
            SetExhaust(false);
            SetUnplayable(true);
            return;
        }

        if (effectLevel >= 2)
        {
            cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[1], true);
            bottomText = new ColoredString(GR.Common.Strings.Ascension.Title(ASCENSION_THRESHOLD_ACT5 + "+"), Settings.CREAM_COLOR);
            baseMagicNumber = magicNumber =  5;
        }
        else
        {
            cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[0], true);
            bottomText = new ColoredString(GR.Common.Strings.Ascension.Title(ASCENSION_THRESHOLD_ACT1 + "+"), Settings.CREAM_COLOR);
            baseMagicNumber = magicNumber =  2;
        }

        cost = costForTurn = 1;
        SetEndOfTurnPlay(true);
        SetEthereal(false);
        SetExhaust(true);
        SetUnplayable(false);
    }
}