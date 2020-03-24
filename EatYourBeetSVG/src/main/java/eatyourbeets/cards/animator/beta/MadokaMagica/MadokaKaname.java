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

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None);

    private int curseCount;
    private int statusCount;

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        curseCount = 0;
        statusCount = 0;

        MoveHindranceUpdateCounts(p.discardPile, p.exhaustPile);

        if (upgraded)
        {
            MoveHindranceUpdateCounts(p.hand, p.exhaustPile);
            MoveHindranceUpdateCounts(p.drawPile, p.exhaustPile);
        }

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainIntellect(statusCount / magicNumber, true).SkipIfZero(true);
            GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, curseCount / magicNumber)).SkipIfZero(true);
        }

        GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
        GameActions.Bottom.VFX(new RainbowCardEffect());
    }

    private void MoveHindranceUpdateCounts(CardGroup source, CardGroup destination)
    {
        boolean move;
        float duration = 0.3f;

        for (AbstractCard card : source.group)
        {
            if (move = (card.type == CardType.CURSE))
            {
                curseCount++;
            }
            else if (move = (card.type == CardType.STATUS))
            {
                statusCount++;
            }

            if (move)
            {
                GameActions.Top.MoveCard(card, source, destination)
                .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
            }
        }
    }
}
