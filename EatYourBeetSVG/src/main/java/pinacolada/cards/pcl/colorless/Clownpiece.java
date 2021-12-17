package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.special.ProvokedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Clownpiece extends PCLCard
{
    public static final PCLCardData DATA = Register(Clownpiece.class).SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.ALL)
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
                PCLActions.Bottom.Add(new EmptyDeckShuffleAction());
            }
        }

        PCLActions.Bottom.Callback(() ->
        {
            if (player.drawPile.size() > 0)
            {
                AbstractCard card = player.drawPile.getTopCard();

                if (card.costForTurn == 0 || card.costForTurn == 1)
                {
                    PCLActions.Top.PlayCard(card, player.drawPile, null)
                            .SpendEnergy(false);
                }
                else
                {
                    PCLActions.Top.Draw(1);
                    for (AbstractMonster mp : PCLGameUtilities.GetEnemies(true)) {
                        PCLActions.Bottom.ApplyPower(p, new ProvokedPower(mp));
                    }
                }
            }
        });

        if (IsStarter() && (CombatStats.TryActivateLimited(cardID)))
        {
            PCLActions.Last.ModifyAllInstances(uuid, c -> ((PCLCard)c).SetExhaust(true));
        }
    }
}

