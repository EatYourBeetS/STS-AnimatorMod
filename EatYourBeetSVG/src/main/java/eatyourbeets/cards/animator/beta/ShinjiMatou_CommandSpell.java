package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.uncommon.Zero;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;


public class ShinjiMatou_CommandSpell extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(ShinjiMatou_CommandSpell.class).SetSkill(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);
    public final AnimatorCard SampleSpellcaster = new Zero();
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
        GameActions.Bottom.SelectFromPile(name, 1, player.discardPile)
                .SetOptions(false, false)
                .SetMessage(DATA.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(this::HasSynergy)
                .AddCallback(cards ->
                {
                    if (!cards.isEmpty())
                    {
                        GameActions.Top.PlayCard(cards.get(0), p.discardPile, m).SetExhaust(false);
                    }
                });
    }
}
