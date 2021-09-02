package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class Venti extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(2).SetSeriesFromClassPackage();

    public Venti()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 2, 0);
        SetAffinity_Star(2, 0, 0);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        AbstractOrb orb = new Air();
        GameActions.Bottom.ChannelOrb(orb);

        // Not using Cycle function here because we need a callback on the drawn cards
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false).SetOptions(true, true, true).AddCallback(cards ->
        {
            int discardedCards = cards.size();
            if (discardedCards > 0)
            {
                GameActions.Bottom.Draw(discardedCards).AddCallback(cardsDrawn ->
                {
                    for (AbstractCard card : cardsDrawn)
                    {
                        if (card.type == CardType.SKILL || card.type == CardType.POWER)
                        {
                            GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);
                            orb.onStartOfTurn();
                            orb.onEndOfTurn();
                        }
                    }
                });
            }
        });

    }

}