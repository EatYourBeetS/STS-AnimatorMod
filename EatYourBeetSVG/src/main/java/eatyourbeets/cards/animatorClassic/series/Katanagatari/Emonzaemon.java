package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Emonzaemon_EntouJyuu;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Emonzaemon extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Emonzaemon.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged);
    static
    {
        DATA.AddPreview(new Emonzaemon_EntouJyuu(), true);
    }

    public Emonzaemon()
    {
        super(DATA);

        Initialize(4, 0);
        SetUpgrade(2, 0);
        SetScaling(0, 1, 0);

        SetSeries(CardSeries.Katanagatari);
        SetMartialArtist();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        if (!CombatStats.HasActivatedLimited(cardID))
        {
            ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
            int size = cardsPlayed.size();
            if (size >= 3)
            {
                boolean threeInARow = true;
                for (int i = 1; i <= 3; i++)
                {
                    if (cardsPlayed.get(size - i).type != CardType.ATTACK)
                    {
                        threeInARow = false;
                    }
                }

                if (threeInARow)
                {
                    CombatStats.TryActivateLimited(cardID);
                    GameActions.Bottom.MakeCardInDrawPile(new Emonzaemon_EntouJyuu())
                    .SetDestination(CardSelection.Bottom)
                    .SetUpgrade(upgraded, false);
                }
            }
        }
    }
}