package eatyourbeets.cards_beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class KaguyaHouraisan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaguyaHouraisan.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public KaguyaHouraisan()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(0, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return (player.drawPile.isEmpty());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.POISON);
        }

        if (info.IsSynergizing)
        {
            for (AbstractCard card : player.hand.group)
            {
                GameActions.Bottom.PlayCard(card, player.hand, null)
                .SpendEnergy(false, true);
            }
        }
    }
}

