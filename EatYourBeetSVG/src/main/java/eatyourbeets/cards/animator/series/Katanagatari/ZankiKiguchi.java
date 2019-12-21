package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class ZankiKiguchi extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(ZankiKiguchi.class.getSimpleName(), EYBCardBadge.Exhaust);

    public ZankiKiguchi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(2,0,2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (MartialArtist.GetScaling() * 2));
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MoveCard(this, AbstractDungeon.player.hand)
            .ShowEffect(true, true);
            GameActions.Bottom.GainAgility(magicNumber);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}