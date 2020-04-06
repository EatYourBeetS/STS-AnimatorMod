package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None);

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 5, 3);
        Initialize(0, 0, -1, 0);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainIntellect(secondaryValue, upgraded);

        if (!EffectHistory.TryActivateLimited(cardID))
        {
            return;
        }

        int hindranceCount = 0;

        hindranceCount += MoveHindranceUpdateCounts(p.discardPile, p.exhaustPile);
        hindranceCount += MoveHindranceUpdateCounts(p.hand, p.exhaustPile);
        hindranceCount += MoveHindranceUpdateCounts(p.drawPile, p.exhaustPile);

        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, hindranceCount / magicNumber)).SkipIfZero(true);

        GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
        GameActions.Bottom.VFX(new RainbowCardEffect());
    }

    private int MoveHindranceUpdateCounts(CardGroup source, CardGroup destination)
    {
        boolean move;
        float duration = 0.3f;

        int hindranceCount = 0;

        for (AbstractCard card : source.group)
        {
            if (GameUtilities.IsCurseOrStatus(card))
            {
                hindranceCount++;

                GameActions.Top.MoveCard(card, source, destination)
                .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
            }
        }

        return hindranceCount;
    }
}
