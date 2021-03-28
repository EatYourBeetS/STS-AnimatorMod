package eatyourbeets.cards.animator.beta.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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

        SetMartialArtist();

        SetSynergy(Synergies.LogHorizon);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        GameActions.Bottom.Callback(this::DrawMinori);

        if (IsStarter())
        {
            GameActions.Bottom.GainBlur(magicNumber);
        }
    }

    private static AbstractCard FindMinori(AbstractMonster target)
    {
        AbstractCard minori = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : player.drawPile.group)
        {
            if (Minori.DATA.ID.equals(c.cardID))
            {
                minori = c;
            }
        }

        return minori;
    }

    private boolean DrawMinori()
    {
        AbstractCard minori = FindMinori(null);

        if (minori != null)
        {
            GameActions.Top.MoveCard(minori, player.hand)
                    .ShowEffect(true, true);
            return true;
        }

        return false;
    }
}