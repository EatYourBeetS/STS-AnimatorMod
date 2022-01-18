package pinacolada.cards.pcl.curse;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.listeners.OnRemovedFromDeckListener;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardSaveData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Curse_AscendersBane extends PCLCard_Curse implements OnRemovedFromDeckListener
{
    public static final PCLCardData DATA = Register(Curse_AscendersBane.class)
            .SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, true);
    public static final int ASCENSION_THRESHOLD = 17;
    public static final int UNNAMED_FORM = 1;

    public ColoredString bottomText;

    public static boolean CheckUnnamedReign()
    {
        return PCLGameUtilities.GetAscensionLevel() >= ASCENSION_THRESHOLD && (GR.PCL.Dungeon.IsUnnamedReign() || (player != null && PCLGameUtilities.GetRelic(UnnamedReignRelic.class) != null));
    }

    public Curse_AscendersBane()
    {
        super(DATA, false);

        Initialize(0, 0, 3);

        SetEthereal(true);
        SetUnplayable(true);
    }

    @Override
    public void OnRemovedFromDeck()
    {
        PCLGameEffects.TopLevelQueue.ShowAndObtain(this);
    }

    @Override
    public void onLoad(PCLCardSaveData data)
    {
        super.onLoad(data);
        if (CheckUnnamedReign() || (auxiliaryData != null && auxiliaryData.form == UNNAMED_FORM))
        {
            AddUnnamedReignEffect();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!dontTriggerOnUseCard)
        {
            PCLActions.Bottom.TakeDamage(magicNumber, AttackEffects.DARK)
            .SetSoundPitch(1.3f, 1.4f);
        }
        else if (auxiliaryData.form == UNNAMED_FORM)
        {
            PCLActions.Bottom.SelectFromPile(name, 1, player.drawPile)
            .SetOptions(CardSelection.Top, false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    PCLActions.Top.Purge(c).ShowEffect(true)
                    .SetTargetPosition(new Vector2(Settings.WIDTH * 0.4f, Settings.HEIGHT * 0.5f))
                    .AddCallback(c, (card, purged) ->
                    {
                        if (purged && PCLGameUtilities.IsHindrance(card))
                        {
                            PCLActions.Top.MoveCard(this, player.drawPile)
                                    .ShowEffect(true, true)
                                    .SetDestination(CardSelection.Top);
                        }
                    });
                }
            });
        }
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
        auxiliaryData.form = UNNAMED_FORM;
        cardText.OverrideDescription(DATA.Strings.EXTENDED_DESCRIPTION[0], true);
        bottomText = new ColoredString(PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[1], ASCENSION_THRESHOLD), Settings.CREAM_COLOR);
        cost = costForTurn = 1;
        playAtEndOfTurn = true;
        SetEthereal(false);
        SetExhaust(true);
    }
}