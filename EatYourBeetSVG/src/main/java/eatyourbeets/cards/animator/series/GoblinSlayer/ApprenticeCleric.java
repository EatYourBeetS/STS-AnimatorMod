package eatyourbeets.cards.animator.series.GoblinSlayer;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.UUID;

public class ApprenticeCleric extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ApprenticeCleric.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public ApprenticeCleric()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light(2);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainBlessing(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlessing(1, upgraded);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        GameActions.Bottom.IncreaseScaling(p.hand, BaseMod.MAX_HAND_SIZE, Affinity.Light, 1)
        .SetFilter(c -> (GameUtilities.HasRedAffinity(c) || GameUtilities.HasOrangeAffinity(c) || GameUtilities.HasGreenAffinity(c)) && buffs.getOrDefault(c.uuid, 0) < magicNumber)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                JUtils.IncrementMapElement(buffs, c.uuid, 1);
            }
        });
    }
}