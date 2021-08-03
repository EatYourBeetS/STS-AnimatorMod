package eatyourbeets.cards.animator.curse;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.listeners.OnRemovedFromDeckListener;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public class Curse_AscendersBane extends AnimatorCard_Curse implements OnRemovedFromDeckListener, CustomSavable<Boolean>
{
    public static final EYBCardData DATA = Register(Curse_AscendersBane.class)
            .SetCurse(-2, EYBCardTarget.None, true);
    public static final int ASCENSION_THRESHOLD = 17;

    public boolean UnnamedReign;
    public ColoredString bottomText;

    public static boolean CheckUnnamedReign()
    {
        return GameUtilities.GetAscensionLevel() >= ASCENSION_THRESHOLD && (GR.Common.Dungeon.IsUnnamedReign() || (player != null && GameUtilities.GetRelic(UnnamedReignRelic.class) != null));
    }

    public Curse_AscendersBane()
    {
        super(DATA, false);

        SetEthereal(true);
    }

    @Override
    public void OnRemovedFromDeck()
    {
        GameEffects.TopLevelQueue.Add(new ShowCardAndObtainEffect(this, Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f));
    }

    @Override
    public Boolean onSave()
    {
        return UnnamedReign;
    }

    @Override
    public void onLoad(Boolean value)
    {
        if (CheckUnnamedReign() || (value != null && value))
        {
            AddUnnamedReignEffect();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (UnnamedReign)
        {
            GameActions.Bottom.SelectFromPile(name, 1, player.drawPile)
            .SetOptions(CardSelection.Top, false)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    final AbstractCard c = cards.get(0);
                    GameActions.Top.Purge(c).ShowEffect(true)
                    .AddCallback(c, (card, __) ->
                    {
                        if (GameUtilities.IsCurseOrStatus(card))
                        {
                            GameActions.Top.MoveCard(this, player.drawPile)
                            .ShowEffect(true, true)
                            .SetDestination(CardSelection.Top);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }

    @Override
    public AbstractCard makeCopy()
    {
        final Curse_AscendersBane copy = (Curse_AscendersBane) super.makeCopy();
        if (CheckUnnamedReign())
        {
            copy.AddUnnamedReignEffect();
        }

        return copy;
    }

    @Override
    public ColoredString GetBottomText()
    {
        return bottomText;
    }

    public void AddUnnamedReignEffect()
    {
        UnnamedReign = true;
        cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[0], true);
        bottomText = new ColoredString(JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[1], ASCENSION_THRESHOLD), Settings.CREAM_COLOR);
    }
}