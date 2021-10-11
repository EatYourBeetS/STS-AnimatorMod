package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class RinTohsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RinTohsaka.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .AddPreviews(OrbCore.GetAllCores(), true);

    public RinTohsaka()
    {
        super(DATA);

        Initialize(0, 5, 0, 1);
        SetUpgrade(0, 1, 0, 1);

        SetAffinity_Water(1, 1, 1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        if (JUtils.Find(GameUtilities.GetIntents(), i -> !i.IsDebuffing()) != null) {
            GameActions.Bottom.GainTemporaryArtifact(secondaryValue);
        }
        else {
            GameActions.Bottom.TriggerOrbPassive(secondaryValue,true,false);
        }


        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.MakeCardInHand(c).SetUpgrade(upgraded, false);
                }
            }));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetUniqueOrbsCount() >= 3 && (tryUse ? CombatStats.TryActivateLimited(cardID) : CombatStats.CanActivateLimited(cardID));
    }
}