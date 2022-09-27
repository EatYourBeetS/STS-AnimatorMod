package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Konosuba.Sylvia;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Sylvia_Chimera extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sylvia_Chimera.class)
            .SetAttack(2, CardRarity.SPECIAL)
            .SetSeries(Sylvia.DATA.Series)
            .SetMaxCopies(1);
    public static final int SEAL_AMOUNT = 6;

    public Sylvia_Chimera()
    {
        super(DATA);

        Initialize(12, 6, 4, 2);
        SetUpgrade(4, 2, 0, 0);

        SetAffinity_Star(2, 0, 3);

        SetRetainOnce(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(SEAL_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.ApplyPoison(player, m, magicNumber);
        GameActions.Bottom.GainPlatedArmor(secondaryValue);

        GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, SEAL_AMOUNT)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Top)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.SealAffinities(c, false);
            }

            GameActions.Bottom.Callback(() -> CombatStats.Affinities.UseAffinity(Affinity.Star, 99));
        });
    }
}