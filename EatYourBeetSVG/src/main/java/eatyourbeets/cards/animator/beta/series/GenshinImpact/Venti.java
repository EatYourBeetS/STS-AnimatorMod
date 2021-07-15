package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Venti extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(2).SetSeriesFromClassPackage();
    private static final int HINDRANCE_THRESHOLD = 2;

    public Venti()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 2, 0);
        SetAffinity_Star(2, 0, 0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        AbstractOrb orb = new Aether();
        GameActions.Bottom.ChannelOrb(orb);

        // Not using Cycle function here because we need a callback on the drawn cards
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false).SetOptions(true, true, true).AddCallback(cards ->
        {
            if (CombatStats.TryActivateSemiLimited(cardID))
            {
                int discardedCards = cards.size();
                if (discardedCards > 0)
                {
                    GameActions.Bottom.Draw(discardedCards).AddCallback(cardsDrawn ->
                    {
                        int hindranceCount = 0;
                        for (AbstractCard card : cardsDrawn)
                        {
                            if (card.type == CardType.SKILL)
                            {
                                GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);
                                orb.onStartOfTurn();
                                orb.onEndOfTurn();
                            }
                            else if (GameUtilities.IsCurseOrStatus(card))
                            {
                                hindranceCount += 1;
                            }
                        }

                        if (hindranceCount >= HINDRANCE_THRESHOLD)
                        {
                            GameActions.Bottom.Draw(secondaryValue);
                        }
                    });
                }
            }
        });

    }

}