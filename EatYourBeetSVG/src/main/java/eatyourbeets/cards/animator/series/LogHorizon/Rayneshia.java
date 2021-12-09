package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Rayneshia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rayneshia.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Rayneshia()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Orange(1);
        SetAffinity_Light(1);
        SetAffinityRequirement(Affinity.General, 5);
    }

    @Override
    public void OnUpgrade() {
        SetHaste(true);
        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(selected ->
        {
            for (AbstractCard c : selected)
            {
                GameActions.Top.MoveCard(c, player.hand, player.drawPile).SetDestination(CardSelection.Top);
            }

            if (IsStarter()) {
                GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                    CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c : selected)
                    {
                        cardGroup.addToBottom(c);
                    }
                    GameActions.Bottom.SelectFromPile(name, 1, cardGroup).AddCallback(cards -> {
                        if (cards.size() > 0) {
                            GameActions.Bottom.Motivate(cards.get(0),1);
                        }
                    });

                });
            }
        });
    }
}