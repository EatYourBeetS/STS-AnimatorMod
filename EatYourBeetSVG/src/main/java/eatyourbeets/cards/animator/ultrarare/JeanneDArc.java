package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class JeanneDArc extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(JeanneDArc.class).SetAttack(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public JeanneDArc()
    {
        super(DATA);

        Initialize(11, 0, 8);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(GameUtilities::IsCurseOrStatus);
        GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
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