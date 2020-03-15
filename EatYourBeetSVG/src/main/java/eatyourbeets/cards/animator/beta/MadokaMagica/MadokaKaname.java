package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
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

    private int CurseCount;
    private int StatusCount;

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        CurseCount = 0;
        StatusCount = 0;

        MoveHindranceUpdateCounts(p.discardPile, p.exhaustPile);

        if (upgraded)
        {
            MoveHindranceUpdateCounts(p.hand, p.exhaustPile);
            MoveHindranceUpdateCounts(p.drawPile, p.exhaustPile);
        }

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainIntellect(StatusCount / 2, true);
            GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, CurseCount / 2)).SkipIfZero(true);
        }

        GameActions.Bottom.VFX(new RainbowCardEffect());
    }

    private void MoveHindranceUpdateCounts(CardGroup source, CardGroup destination)
    {
        float duration = 0.3f;

        for (AbstractCard card : source.group)
        {
            if (GameUtilities.IsCurseOrStatus(card))
            {
                if (card.type == CardType.CURSE) {
                    CurseCount++;
                }
                else if (card.type == CardType.STATUS) {
                    StatusCount++;
                }

                GameActions.Top.MoveCard(card, source, destination)
                .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
            }
        }
    }
}
