package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public class KaguyaHouraisan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaguyaHouraisan.class).SetAttack(1,CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KaguyaHouraisan()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Orange(1, 0, 0);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.POISON);
        }

        if (isSynergizing)
        {
            List<AbstractCard> cardsToPlay = new ArrayList<>();

            cardsToPlay.addAll(player.hand.group);

            for (AbstractCard card : cardsToPlay)
            {
                GameActions.Bottom.PlayCard(card, player.hand, null)
                        .SpendEnergy(false);
            }
        }
    }
}

