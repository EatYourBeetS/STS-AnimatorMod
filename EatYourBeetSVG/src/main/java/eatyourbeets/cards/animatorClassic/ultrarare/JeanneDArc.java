package eatyourbeets.cards.animatorClassic.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class JeanneDArc extends AnimatorClassicCard_UltraRare
{
    public static final EYBCardData DATA = Register(JeanneDArc.class).SetAttack(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public JeanneDArc()
    {
        super(DATA);

        Initialize(11, 0, 8);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 0, 1);

        SetSeries(CardSeries.Fate);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(GameUtilities::IsHindrance);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainTemporaryHP(magicNumber);
            GameActions.Bottom.GainArtifact(1);
        }
    }
}