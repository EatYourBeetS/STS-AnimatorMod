package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ProvokedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Clownpiece extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Clownpiece.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    public Clownpiece()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Star(1, 0 ,0);
        SetInnate(true);
        SetExhaust(true);

    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (!CombatStats.HasActivatedLimited(cardID))
        {
            SetExhaust(!IsStarter());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.drawPile.isEmpty())
        {
            if (player.discardPile.size() > 0)
            {
                GameActions.Bottom.Add(new EmptyDeckShuffleAction());
            }
        }

        GameActions.Bottom.Callback(() ->
        {
            if (player.drawPile.size() > 0)
            {
                AbstractCard card = player.drawPile.getTopCard();

                if (card.costForTurn == 0 || card.costForTurn == 1)
                {
                    GameActions.Top.PlayCard(card, player.drawPile, null)
                            .SpendEnergy(false);
                }
                else
                {
                    GameActions.Top.Draw(1);
                    for (AbstractMonster mp : GameUtilities.GetEnemies(true)) {
                        GameActions.Bottom.ApplyPower(p, new ProvokedPower(mp));
                    }
                }
            }
        });

        if (IsStarter() && (CombatStats.TryActivateLimited(cardID)))
        {
            GameActions.Last.ModifyAllInstances(uuid, c -> ((EYBCard)c).SetExhaust(true));
        }
    }
}

