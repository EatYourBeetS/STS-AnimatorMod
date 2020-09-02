package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Hinaichigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hinaichigo.class)
    		.SetSkill(2, CardRarity.COMMON);

    public Hinaichigo()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 1);

        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        GameActions.Bottom.ApplyWeak(p, m, magicNumber);

        if (player.stance.ID.equals(ForceStance.STANCE_ID))
        {
            GameActions.Bottom.ApplyPoison(p, m, secondaryValue);
        }
        else
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
                    .SetOptions(false, false, false)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard card : cards)
                        {
                            GameActions.Bottom.MoveCard(card, p.hand, p.drawPile)
                                    .ShowEffect(true,true)
                                    .SetDestination(CardSelection.Top);
                        }
                        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
                    });
        }
    }
}
