package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class ZankiKiguchi extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register_Old(ZankiKiguchi.class);

    public ZankiKiguchi()
    {
        super(ID, 0, CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(2, 0, 2);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MoveCard(this, player.hand)
            .ShowEffect(true, true);
            GameActions.Bottom.GainAgility(magicNumber);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }
}