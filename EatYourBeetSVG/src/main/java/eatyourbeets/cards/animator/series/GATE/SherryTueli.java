package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class SherryTueli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SherryTueli.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public SherryTueli()
    {
        super(DATA);

        Initialize(0, 5, 7);
        SetUpgrade(0, 2, 2);

        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(Affinity.Light, 5);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        CalculateHeal();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.DiscardFromHand(name, 2, false)
        .SetOptions(false, true, false)
        .SetFilter(c -> c.type == CardType.ATTACK && !c.purgeOnUse && !c.hasTag(PURGE))
        .AddCallback(cards ->
        {
            if (cards.size() >= 2)
            {
                if (CalculateHeal() > 0)
                {
                    GameActions.Bottom.RecoverHP(heal);
                }
                if (JUtils.Find(cards, c -> !GameUtilities.HasOrangeAffinity(c)) != null) {
                    GameActions.Bottom.ObtainAffinityToken(Affinity.Orange, false);
                }
            }
        });

        if (TrySpendAffinity(Affinity.Light)) {
            GameActions.Bottom.DrawNextTurn(1);
        }
    }

    protected int CalculateHeal()
    {
        return heal = Math.min(magicNumber, GameActionManager.playerHpLastTurn - player.currentHealth);
    }
}