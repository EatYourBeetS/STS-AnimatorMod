package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YuiTsuruno extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(YuiTsuruno.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(9, 0, 0);
        SetUpgrade(4, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
        .ShowEffect(true, true)
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                if (cards.get(0).type == CardType.ATTACK)
                {
                    GameActions.Bottom.Add(new CreateRandomCurses(1, p.discardPile));
                }
                else
                {
                    GameActions.Bottom.ChannelOrb(new Fire(), true);
                }
            }
        });
    }
}