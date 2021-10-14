package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.replacement.PlayerCurlUpPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Victim extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Victim.class)
            .SetSkill(3, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Victim()
    {
        super(DATA);

        Initialize(0,0, 32, 10);
        SetUpgrade(0,0,0,10);

        SetAffinity_Light(2);
        SetAffinity_Dark(2);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 6);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal && (GameUtilities.HasDarkAffinity(c)))
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        GameUtilities.Retain(card);
                    }
                });
        GameActions.Bottom.StackPower(new PlayerCurlUpPower(player, magicNumber));

        if (info.CanActivateLimited && GameUtilities.GetHealthPercentage(player) < secondaryValue * 0.01f && CheckAffinity(Affinity.Light) && info.TryActivateLimited()) {
            GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, secondaryValue));
            GameActions.Last.Purge(this);
        }
    }
}