package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;


public class ShinjiMatou_CommandSpell extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(ShinjiMatou_CommandSpell.class).SetSkill(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public ShinjiMatou_CommandSpell()
    {
        super(DATA);

        SetCostUpgrade(0);
        SetRetain(true);
        SetPurge(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SelectFromPile(name, 1, p.discardPile)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .SetFilter(this::HasSynergy)
        .AddCallback(m, (enemy, cards) ->
        {
            if (!cards.isEmpty())
            {
                GameActions.Top.PlayCard(cards.get(0), player.discardPile, (AbstractMonster) enemy).SetExhaust(false);
            }
        });
    }
}
