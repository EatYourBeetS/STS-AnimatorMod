package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public class KaguyaHouraisan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaguyaHouraisan.class).SetAttack(1,CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public KaguyaHouraisan()
    {
        super(DATA);

        Initialize(10, 0, 4);
        SetUpgrade(0, 0, 1);
        SetScaling(2, 0, 0);

        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
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

            for (AbstractCard card : player.hand.group)
            {
                cardsToPlay.add(card);
            }

            for (AbstractCard card : cardsToPlay)
            {
                GameActions.Bottom.PlayCard(card, player.drawPile, null)
                        .SpendEnergy(false);
            }
        }
    }
}

