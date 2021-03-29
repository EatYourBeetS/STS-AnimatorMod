package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Tohya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tohya.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);
    static
    {
        DATA.AddPreview(new Minori(), false);
    }

    public Tohya()
    {
        super(DATA);

        Initialize(5, 0, 1, 0);
        SetUpgrade(3, 0, 0, 0);

        SetSynergy(Synergies.LogHorizon);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.Draw(1)
        .SetFilter(c -> Minori.DATA.ID.equals(c.cardID), false);

        if (IsStarter())
        {
            GameActions.Bottom.GainBlur(magicNumber);
        }
    }
}